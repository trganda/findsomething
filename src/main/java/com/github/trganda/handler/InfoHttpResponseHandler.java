package com.github.trganda.handler;

import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.proxy.http.ProxyResponseHandler;
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction;
import burp.api.montoya.proxy.http.ProxyResponseToBeSentAction;
import com.github.trganda.FindSomething;
import com.github.trganda.config.Config;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.config.Scope;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.RequestDetailModel;
import com.github.trganda.model.cache.CachePool;
import com.github.trganda.utils.Utils;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoHttpResponseHandler implements ProxyResponseHandler {

  private final Long id = 0L;
  private final ExecutorService pool;
  private final List<DataChangeListener> listeners = new ArrayList<>();

  public InfoHttpResponseHandler(ExecutorService pool) {
    this.pool = pool;
  }

  @Override
  public ProxyResponseReceivedAction handleResponseReceived(
      InterceptedResponse interceptedResponse) {

    if (filter(interceptedResponse)) {
      return ProxyResponseReceivedAction.continueWith(interceptedResponse);
    }

    HttpRequest req = interceptedResponse.request();
    FindSomething.API.logging().logToOutput(req.url());
    pool.submit(
        () -> {
          process(interceptedResponse);
        });

    return ProxyResponseReceivedAction.continueWith(interceptedResponse);
  }

  @Override
  public ProxyResponseToBeSentAction handleResponseToBeSent(
      InterceptedResponse interceptedResponse) {
    return ProxyResponseToBeSentAction.continueWith(interceptedResponse);
  }

  public void registerDataChangeListener(DataChangeListener listener) {
    this.listeners.add(listener);
  }

  // private void process(InterceptedResponse interceptedResponse) {
  //   HttpRequest req = interceptedResponse.request();
  //   String body = interceptedResponse.body().toString();
  //   String[] results = match(body, null);

  //   List<InfoDataModel> data = new ArrayList<>();
  //   for (String result : results) {
  //     InfoDataModel infoDataModel = new InfoDataModel(result);
  //     data.add(infoDataModel);

  //     // set request info
  //     String hash = Utils.calHash(result);
  //     RequestDataModel requestDataModel =
  //         new RequestDataModel(
  //             interceptedResponse.messageId(),
  //             req.path(),
  //             req.httpService().host(),
  //             interceptedResponse.statusCode(),
  //             "1");
  //     CachePool.addRequestDataModel(hash, requestDataModel);

  //     // set request and response for future use
  //     String reqHash = Utils.calHash(req.path(), req.httpService().host());
  //     CachePool.putInterceptedResponse(reqHash, interceptedResponse);
  //   }

  //   for (DataChangeListener listener : listeners) {
  //     listener.onDataChanged(data);
  //   }
  // }

  private void process(InterceptedResponse interceptedResponse) {
    HttpRequest req = interceptedResponse.request();
    Config.getInstance().getRules().getGroups().stream()
        .forEach(
            g ->
                g.getRule().stream()
                    .forEach(
                        r -> {
                          if (!r.isEnabled()) {
                            return;
                          }
                          String[] results = this.match(interceptedResponse, r);
                          List<InfoDataModel> data = new ArrayList<>();
                          for (String result : results) {
                            InfoDataModel infoDataModel = new InfoDataModel(result);
                            data.add(infoDataModel);

                            DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("HH:mm:ss d MMM yyyy");
                            // set request info
                            String hash = Utils.calHash(result);
                            RequestDetailModel requestDataModel =
                                new RequestDetailModel(
                                    interceptedResponse.messageId(),
                                    req.path(),
                                    req.httpService().host(),
                                    interceptedResponse.statusCode(),
                                    ZonedDateTime.now().format(formatter));
                            CachePool.addRequestDataModel(hash, requestDataModel);

                            // set request and response for future use
                            String reqHash = Utils.calHash(req.path(), req.httpService().host());
                            CachePool.putInterceptedResponse(reqHash, interceptedResponse);
                          }

                          for (DataChangeListener listener : listeners) {
                            listener.onDataChanged(data);
                          }
                        }));
  }

  /**
   * Determines whether the intercepted response should be filtered out.
   *
   * <p>This method is invoked by a separate thread to not block the proxy server.
   *
   * <p>This method filters out the response if:
   * <ul>
   *   <li>The {@code pathWithoutQuery()} of the request ends with one of the suffixes
   *       specified in the configuration.
   *   <li>The {@code host()} of the request matches one of the hosts specified in the
   *       configuration.
   *   <li>The status code of the response matches one of the status codes specified in the
   *       configuration.
   *   <li>The body of the response is empty.
   * </ul>
   *
   * <p>This method returns {@code false} if the response should be filtered out and
   * {@code true} otherwise.
   */
  private boolean filter(InterceptedResponse interceptedResponse) {
    HttpRequest req = interceptedResponse.request();
    String path = req.pathWithoutQuery();
    for (String suffix : Config.getInstance().getSuffixes()) {
      if (path.endsWith(suffix)) {
        return true;
      }
    }

    for (String host : Config.getInstance().getHosts()) {
      if (req.httpService().host().equals(host)) {
        return true;
      }
    }

    for (String status : Config.getInstance().getStatus()) {
      if (String.valueOf(interceptedResponse.statusCode()).equals(status)) {
        return true;
      }
    }

    return interceptedResponse.body().length() == 0;
  }

  private String[] match(InterceptedResponse interceptedResponse, Rule rule) {
    Scope scope = rule.getScope();
    String body = interceptedResponse.body().toString();

    List<String> matchList = new ArrayList<>();
    Pattern pattern = Pattern.compile(rule.getRegex());
    switch (scope) {
      case RESPONSE_BODY:
        matchList.addAll(Arrays.asList(match(body, pattern)));
        break;
      case RESPONSE_HEADER:
        interceptedResponse
            .headers()
            .forEach(
                h -> {
                  matchList.addAll(Arrays.asList(match(h.value(), pattern)));
                });
        break;
      default:
        break;
    }
    return matchList.toArray(new String[0]);
  }

  private String[] match(String text, Pattern pattern) {
    Matcher matcher = pattern.matcher(text);
    HashSet<String> set = new HashSet<>();

    while (matcher.find()) {
      set.add(matcher.group());
    }

    return set.toArray(new String[0]);
  }
}

package com.github.trganda.handler;

import burp.api.montoya.http.message.params.HttpParameterType;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.proxy.http.ProxyResponseHandler;
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction;
import burp.api.montoya.proxy.http.ProxyResponseToBeSentAction;
import com.github.trganda.cleaner.Cleaner;
import com.github.trganda.config.Config;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.config.Scope;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.RequestDetailModel;
import com.github.trganda.utils.Utils;
import com.github.trganda.utils.cache.CachePool;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoHttpResponseHandler implements ProxyResponseHandler {

  private Long id = 0L;
  private final ExecutorService pool;
  private final List<DataChangeListener> listeners;
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss d MMM yyyy");
  private Cleaner cleaner;

  public InfoHttpResponseHandler(ExecutorService pool) {
    this.pool = pool;
    this.cleaner = new Cleaner();
    this.listeners = new CopyOnWriteArrayList<>();
  }

  @Override
  public ProxyResponseReceivedAction handleResponseReceived(
      InterceptedResponse interceptedResponse) {

    if (filter(interceptedResponse)) {
      return ProxyResponseReceivedAction.continueWith(interceptedResponse);
    }

    HttpRequest req = interceptedResponse.request();

    pool.submit(
        () -> {
          // collection host info
          CachePool.getInstance().addHost(req.httpService().host());
          List<String> domains = CachePool.getInstance().getHosts();
          List<String> suggestions = Utils.aggregator(domains);
          suggestions.stream().forEach(CachePool.getInstance()::addHost);
        });

    pool.submit(
        () -> {
          // FindSomething.API.logging().logToOutput("processing, " + req.url());
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

  private void process(InterceptedResponse interceptedResponse) {
    HttpRequest req = interceptedResponse.request();
    Config.getInstance().getRules().getGroups().stream()
        .forEach(
            g ->
                g.getRule().stream()
                    .forEach(
                        r -> {
                          if (r.isEnabled()) {
                            cleaner.setData(Arrays.asList(this.match(interceptedResponse, r)));
                            String[] results = cleaner.clean();
                            List<InfoDataModel> data = new ArrayList<>();
                            // FindSomething.API
                            //     .logging()
                            //     .logToOutput("rule:" + r.getName() + " count: " +
                            // results.length);
                            for (String result : results) {
                              InfoDataModel infoDataModel =
                                  new InfoDataModel(
                                      id, r.getName(), result, req.httpService().host());
                              id = id + 1;
                              data.add(infoDataModel);

                              CachePool.getInstance().addInfoDataModel(g.getGroup(), infoDataModel);

                              // set request info
                              String hash = Utils.calHash(result);
                              RequestDetailModel requestDataModel =
                                  new RequestDetailModel(
                                      interceptedResponse.messageId(),
                                      req.method(),
                                      req.path(),
                                      req.httpService().host(),
                                      interceptedResponse.statusCode(),
                                      ZonedDateTime.now().format(formatter));
                              CachePool.getInstance().addRequestDataModel(hash, requestDataModel);

                              // FindSomething.API.logging().logToOutput("debug");
                              // set request and response
                              String reqHash = Utils.calHash(req.path(), req.httpService().host());
                              CachePool.getInstance()
                                  .putInterceptedResponse(reqHash, interceptedResponse);
                            }

                            for (DataChangeListener listener : listeners) {
                              listener.onDataChanged();
                            }
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
    HttpRequest req = interceptedResponse.request();

    String reqBody = req.bodyToString();
    String rspBody = interceptedResponse.bodyToString();

    List<String> matchList = new ArrayList<>();
    Pattern pattern =
        Pattern.compile(rule.getRegex(), rule.isSensitive() ? 0 : Pattern.CASE_INSENSITIVE);
    switch (scope) {
      case RESPONSE_BODY:
        matchList.addAll(Arrays.asList(match(rspBody, pattern)));
        break;
      case RESPONSE_HEADER:
        interceptedResponse
            .headers()
            .forEach(
                h -> {
                  matchList.addAll(Arrays.asList(match(h.toString(), pattern)));
                });
      case REQUEST_BODY:
        matchList.addAll(Arrays.asList(match(reqBody, pattern)));
        break;
      case REQUEST_HEADER:
        req.headers()
            .forEach(
                h -> {
                  matchList.addAll(Arrays.asList(match(h.toString(), pattern)));
                });
      case REQUEST_PATH:
        matchList.addAll(Arrays.asList(match(req.pathWithoutQuery(), pattern)));
        break;
      case REQUEST_QUERY:
        req.parameters().stream()
            .filter(p -> p.type() != HttpParameterType.COOKIE)
            .forEach(
                p -> matchList.addAll(Arrays.asList(match(p.name() + "=" + p.value(), pattern))));
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

package com.github.trganda.handler;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.proxy.http.ProxyResponseHandler;
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction;
import burp.api.montoya.proxy.http.ProxyResponseToBeSentAction;
import com.github.trganda.FindSomething;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.RequestDataModel;
import com.github.trganda.model.cache.CachePool;
import com.github.trganda.utils.Utils;

import java.util.ArrayList;
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
    public ProxyResponseReceivedAction handleResponseReceived(InterceptedResponse interceptedResponse) {

        if (filter(interceptedResponse)) {
            return ProxyResponseReceivedAction.continueWith(interceptedResponse);
        }

        HttpRequest req = interceptedResponse.request();
        String path = req.pathWithoutQuery();
        if (path.endsWith(".js")) {
            FindSomething.api.logging().logToOutput(req.url());
            pool.submit(() -> {
                process(interceptedResponse);
            });
        }

        return ProxyResponseReceivedAction.continueWith(interceptedResponse);
    }

    @Override
    public ProxyResponseToBeSentAction handleResponseToBeSent(InterceptedResponse interceptedResponse) {
        return ProxyResponseToBeSentAction.continueWith(interceptedResponse);
    }

    public void registerDataChangeListener(DataChangeListener listener) {
        this.listeners.add(listener);
    }

    private void process(InterceptedResponse interceptedResponse) {
        HttpRequest req = interceptedResponse.request();
        String body = interceptedResponse.body().toString();
        String[] results = match(body);

        List<InfoDataModel> data = new ArrayList<>();
        for (String result : results) {
            InfoDataModel infoDataModel = new InfoDataModel(result);
            data.add(infoDataModel);

            // set request info
            String hash = Utils.calHash(result);
            RequestDataModel requestDataModel = new RequestDataModel(interceptedResponse.messageId(), req.path(), req.httpService().host(), interceptedResponse.statusCode(), "1");
            CachePool.addRequestDataModel(hash, requestDataModel);

            // set request and response for future use
            String reqHash = Utils.calHash(req.path(), req.httpService().host());
            CachePool.putInterceptedResponse(reqHash, interceptedResponse);
        }

        for (DataChangeListener listener : listeners) {
            listener.onDataChanged(data);
        }
    }

    private boolean filter(InterceptedResponse interceptedResponse) {
        HttpRequest req = interceptedResponse.request();
        if (interceptedResponse.statusCode() > 200 && interceptedResponse.statusCode() < 400) {
            return true;
        }

        if (interceptedResponse.body().length() == 0) {
            return true;
        }

        return false;
    }

    private String[] match(String rspBody) {
        Pattern pattern = Pattern.compile("(?:\"|')(((?:[a-zA-Z]{1,10}://|//)[^\"'/]{1,}\\.[a-zA-Z]{2,}[^\"']{0,})|((?:/|\\.\\./|\\./)[^\"'><,;|*()(%%$^/\\\\\\[\\]][^\"'><,;|()]{1,})|([a-zA-Z0-9_\\-/]{1,}/[a-zA-Z0-9_\\-/]{1,}\\.(?:[a-zA-Z]{1,4}|action)(?:[\\?|#][^\"|']{0,}|))|([a-zA-Z0-9_\\-/]{1,}/[a-zA-Z0-9_\\-/]{3,}(?:[\\?|#][^\"|']{0,}|))|([a-zA-Z0-9_\\-]{1,}\\.(?:\\w)(?:[\\?|#][^\"|']{0,}|)))(?:\"|')");
        Matcher matcher = pattern.matcher(rspBody);

        HashSet<String> set = new HashSet<>();

        while (matcher.find()) {
            set.add(matcher.group());
        }

        return set.toArray(new String[0]);
    }
}

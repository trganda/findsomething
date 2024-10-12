package com.github.trganda.handler;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.proxy.http.ProxyResponseHandler;
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction;
import burp.api.montoya.proxy.http.ProxyResponseToBeSentAction;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoHttpResponseHandler implements ProxyResponseHandler {

    private final MontoyaApi api;

    public InfoHttpResponseHandler(MontoyaApi api) {
        this.api = api;
    }

    @Override
    public ProxyResponseReceivedAction handleResponseReceived(InterceptedResponse interceptedResponse) {

        if (!filter(interceptedResponse)) {
            HttpRequest req = interceptedResponse.request();
            String url = req.url();

            if (url.endsWith(".js")) {
                api.logging().logToOutput(req.url());
            }
        }

        return ProxyResponseReceivedAction.continueWith(interceptedResponse);
    }

    @Override
    public ProxyResponseToBeSentAction handleResponseToBeSent(InterceptedResponse interceptedResponse) {
        return ProxyResponseToBeSentAction.continueWith(interceptedResponse);
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
        Pattern pattern = Pattern.compile("['\"](?:/|\\.\\./|\\./)[^/>< )({}\\,\'\"\\\\]*([^>< )({}\\,\'\"\\\\])*?['\"]");
        Matcher matcher = pattern.matcher(rspBody);

        ArrayList<String> list = new ArrayList<>();

        while (matcher.find()) {
            list.add(matcher.group());
        }

        return list.toArray(new String[0]);
    }
}

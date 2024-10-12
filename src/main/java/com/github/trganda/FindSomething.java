package com.github.trganda;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.github.trganda.componets.Dashboard;
import com.github.trganda.handler.InfoHttpResponseHandler;

public class FindSomething implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        api.proxy().registerResponseHandler(new InfoHttpResponseHandler(api));
        api.userInterface().registerSuiteTab("FindSomething", new Dashboard(api));
    }
}

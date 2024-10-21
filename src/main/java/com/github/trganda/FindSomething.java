package com.github.trganda;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.github.trganda.components.ExtensionFrame;
import com.github.trganda.handler.InfoHttpResponseHandler;
import com.github.trganda.handler.UnloadHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindSomething implements BurpExtension {

    @Override
    public void initialize(MontoyaApi api) {
        ExecutorService pool = Executors.newCachedThreadPool();

        InfoHttpResponseHandler handler = new InfoHttpResponseHandler(api, pool);
        ExtensionFrame extensionFrame = new ExtensionFrame(api);

        handler.registerDataChangeListener(extensionFrame.getFilterSplitPane());
        // register HTTP response handler
        api.proxy().registerResponseHandler(handler);
        api.userInterface().registerSuiteTab("FindSomething", extensionFrame);

        // shutdown thread pool while unloading
        api.extension().registerUnloadingHandler(new UnloadHandler(pool));
    }
}

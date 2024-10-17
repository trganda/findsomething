package com.github.trganda;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.github.trganda.componets.MainPane;
import com.github.trganda.handler.InfoHttpResponseHandler;
import com.github.trganda.handler.UnloadHandler;
import com.github.trganda.model.cache.CachePool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindSomething implements BurpExtension {

    @Override
    public void initialize(MontoyaApi api) {
        ExecutorService pool = Executors.newCachedThreadPool();

        InfoHttpResponseHandler handler = new InfoHttpResponseHandler(api, pool);
        MainPane mainPane = new MainPane(api);

        handler.registerDataChangeListener(mainPane.getFilterSplitPane());
        // register HTTP response handler
        api.proxy().registerResponseHandler(handler);
        api.userInterface().registerSuiteTab("FindSomething", mainPane);

        // shutdown thread pool while unloading
        api.extension().registerUnloadingHandler(new UnloadHandler(pool));
    }
}

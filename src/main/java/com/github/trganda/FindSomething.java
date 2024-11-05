package com.github.trganda;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.github.trganda.components.ExtensionFrame;
import com.github.trganda.config.Config;
import com.github.trganda.handler.InfoHttpResponseHandler;
import com.github.trganda.handler.UnloadHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindSomething implements BurpExtension {

  public static MontoyaApi API;

  @Override
  public void initialize(MontoyaApi api) {
    FindSomething.API = api;

    // loading the default configuration file to ${home}/.config
    Config.loadConfig();

    // loading the default rules file to ${home}/.config
    Config.loadRules();

    ExecutorService pool = Executors.newCachedThreadPool();
    InfoHttpResponseHandler handler = new InfoHttpResponseHandler(pool);
    ExtensionFrame extensionFrame = new ExtensionFrame();
    handler.registerDataChangeListener(extensionFrame.getFilterSplitPane());

    // register HTTP response handler
    api.proxy().registerResponseHandler(handler);
    api.userInterface().registerSuiteTab("FindSomething", extensionFrame);

    // shutdown thread pool while unloading
    api.extension().registerUnloadingHandler(new UnloadHandler(pool));
  }
}

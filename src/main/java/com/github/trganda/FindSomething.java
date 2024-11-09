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

  private InfoHttpResponseHandler handler;

  private ExtensionFrame extensionFrame;

  private static FindSomething fd;

  public static FindSomething getInstance() {
    return fd;
  }

  @Override
  public void initialize(MontoyaApi api) {
    fd = this;
    FindSomething.API = api;

    // loading the default configuration file to ${home}/.config
    Config config = Config.getInstance();
    Config.getInstance().registerConfigListener(config);

    ExecutorService pool = Executors.newSingleThreadExecutor();
    handler = new InfoHttpResponseHandler(pool);
    extensionFrame = new ExtensionFrame();

    // register HTTP response handler
    api.proxy().registerResponseHandler(handler);
    api.userInterface().registerSuiteTab("FindSomething", extensionFrame);

    // shutdown thread pool while unloading
    api.extension().registerUnloadingHandler(new UnloadHandler(pool));
  }

  public InfoHttpResponseHandler getHandler() {
    return handler;
  }
}

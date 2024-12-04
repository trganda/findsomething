package com.github.trganda;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.github.trganda.components.ExtensionFrame;
import com.github.trganda.components.config.Editor;
import com.github.trganda.config.Config;
import com.github.trganda.controller.Mediator;
import com.github.trganda.controller.config.FilterController;
import com.github.trganda.controller.config.RuleController;
import com.github.trganda.controller.config.RuleEditorController;
import com.github.trganda.handler.InfoHttpResponseHandler;
import com.github.trganda.handler.UnloadHandler;
import com.github.trganda.model.RuleModel;
import java.awt.Frame;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

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
    FindSomething.API = api;
    fd = this;

    // UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
    // for (UIManager.LookAndFeelInfo look : looks) {
    //   api.logging().logToOutput("LookAndFeel: " + look.getName());
    // }
    // UIDefaults defaults = UIManager.getLookAndFeelDefaults();
    // Enumeration<Object> keys = defaults.keys();
    // while (keys.hasMoreElements()) {
    //     Object key = keys.nextElement();
    //     Object value = defaults.get(key);
    //     api.logging().logToOutput(String.format("%-40s : %s%n", key, value));
    // }
    // api.logging().logToOutput("Button hover: " + UIManager.get("Button.hoverBackground"));

    // loading the default configuration file to ${home}/.config
    Config config = Config.getInstance();
    Config.getInstance().registerConfigListener(config);

    ExecutorService pool = Executors.newSingleThreadExecutor();
    handler = new InfoHttpResponseHandler(pool);
    extensionFrame = new ExtensionFrame();
    extensionFrame.getConfig().getRulePane();

    Mediator mediator = new Mediator(null);

    new RuleController(extensionFrame.getConfig().getRulePane().getRuleInnerPane(), mediator);

    Frame pFrame = api.userInterface().swingUtils().suiteFrame();
    Editor editor = new Editor(pFrame);
    new RuleEditorController(editor, new RuleModel(), mediator);
    new FilterController(extensionFrame.getConfig().getBlackListPane().getBlackListInnerPane());

    // DashboardController dashboardController =
    //     new DashboardController(extensionFrame.getDashboard());
    // handler.registerDataChangeListener(dashboardController);

    api.userInterface().applyThemeToComponent(extensionFrame);

    // register HTTP response handler
    api.proxy().registerResponseHandler(handler);
    api.userInterface().registerSuiteTab("FindSomething", extensionFrame);

    // shutdown thread pool while unloading
    api.extension().registerUnloadingHandler(new UnloadHandler(pool));
  }
}

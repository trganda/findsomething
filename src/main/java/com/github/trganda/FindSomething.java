package com.github.trganda;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.github.trganda.components.ExtensionFrame;
import com.github.trganda.components.dashboard.InformationDetailsPane;
import com.github.trganda.components.dashboard.InformationPane;
import com.github.trganda.components.dashboard.RequestPane;
import com.github.trganda.config.Config;
import com.github.trganda.controller.config.FilterController;
import com.github.trganda.controller.config.RuleController;
import com.github.trganda.controller.config.RuleEditorController;
import com.github.trganda.controller.dashboard.InfoController;
import com.github.trganda.controller.dashboard.InfoDetailController;
import com.github.trganda.controller.dashboard.InfoFilterController;
import com.github.trganda.controller.dashboard.OptionsButtonController;
import com.github.trganda.handler.InfoHttpResponseHandler;
import com.github.trganda.handler.UnloadHandler;
import com.github.trganda.model.RuleModel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import lombok.Getter;

@Getter
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

    RuleEditorController editorController = new RuleEditorController(new RuleModel());
    new RuleController(
        extensionFrame.getConfig().getRulePane().getRuleInnerPane(), editorController);

    new FilterController(extensionFrame.getConfig().getBlackListPane().getBlackListInnerPane());

    InformationDetailsPane informationDetailsPane =
        extensionFrame.getDashboard().getRequestSplitFrame().getInformationDetailsPane();
    JButton optionsButton = informationDetailsPane.getFilterPane().getOptionsButton();
    new OptionsButtonController(optionsButton);

    RequestPane requestPane = extensionFrame.getDashboard().getRequestSplitFrame().getRequestPane();
    InfoDetailController infoDetailController =
        new InfoDetailController(informationDetailsPane, requestPane);
    InformationPane informationPane = extensionFrame.getDashboard().getInformationPane();

    InfoController infoController = new InfoController(informationPane, infoDetailController);
    handler.registerDataChangeListener(infoController);

    JButton filterButton = informationDetailsPane.getFilterPane().getFilterButton();
    new InfoFilterController(filterButton, infoController);

    // register HTTP response handler
    api.proxy().registerResponseHandler(handler);
    api.userInterface().registerSuiteTab("FindSomething", extensionFrame);

    // shutdown thread pool while unloading
    api.extension().registerUnloadingHandler(new UnloadHandler(pool));
  }
}

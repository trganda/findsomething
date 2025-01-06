package com.github.trganda;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.github.trganda.components.ExtensionFrame;
import com.github.trganda.components.dashboard.InformationDetailsPanel;
import com.github.trganda.components.dashboard.InformationPanel;
import com.github.trganda.components.dashboard.RequestPanel;
import com.github.trganda.components.dashboard.StatusPanel;
import com.github.trganda.config.ConfigManager;
import com.github.trganda.controller.config.FilterController;
import com.github.trganda.controller.config.GroupController;
import com.github.trganda.controller.config.RuleController;
import com.github.trganda.controller.config.RuleEditorController;
import com.github.trganda.controller.dashboard.InfoController;
import com.github.trganda.controller.dashboard.InfoDetailController;
import com.github.trganda.controller.dashboard.InfoFilterController;
import com.github.trganda.controller.dashboard.OptionsButtonController;
import com.github.trganda.handler.InfoHttpResponseHandler;
import com.github.trganda.handler.UnloadHandler;
import com.github.trganda.model.RuleModel;
import com.github.trganda.utils.VersionUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import lombok.Getter;

@Getter
public class FindSomething implements BurpExtension {

  public static MontoyaApi API;

  private InfoHttpResponseHandler handler;

  private ExtensionFrame extensionFrame;

  @Override
  public void initialize(MontoyaApi api) {
    FindSomething.API = api;

    api.extension().setName(String.format("FindSomething (%s)", VersionUtil.getVersion()));

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
    ConfigManager configManager = ConfigManager.getInstance();
    ConfigManager.getInstance().registerConfigListener(configManager);

    ExecutorService pool = Executors.newSingleThreadExecutor();
    handler = new InfoHttpResponseHandler(pool);
    extensionFrame = new ExtensionFrame();
    extensionFrame.getConfig().getRulePanel();

    RuleEditorController editorController = new RuleEditorController(new RuleModel());
    new RuleController(
        extensionFrame.getConfig().getRulePanel().getRuleInnerPanel(), editorController);
    new GroupController(extensionFrame.getConfig().getRulePanel().getGroupPanel());
    new FilterController(extensionFrame.getConfig().getBlackListPane().getBlackListInnerPane());

    InformationDetailsPanel informationDetailsPanel =
        extensionFrame.getDashboard().getRequestSplitFrame().getInformationDetailsPanel();
    JButton optionsButton = informationDetailsPanel.getFilterPanel().getOptionsButton();
    new OptionsButtonController(optionsButton);

    RequestPanel requestPanel =
        extensionFrame.getDashboard().getRequestSplitFrame().getRequestPanel();
    InfoDetailController infoDetailController =
        new InfoDetailController(informationDetailsPanel, requestPanel);
    InformationPanel informationPanel = extensionFrame.getDashboard().getInformationPanel();
    StatusPanel statusPanel = extensionFrame.getDashboard().getStatusPanel();

    InfoController infoController =
        new InfoController(informationPanel, statusPanel, infoDetailController);
    handler.registerDataChangeListener(infoController);

    JButton filterButton = informationDetailsPanel.getFilterPanel().getFilterButton();
    new InfoFilterController(filterButton, infoController);

    // register HTTP response handler
    api.proxy().registerResponseHandler(handler);
    api.userInterface().registerSuiteTab("FindSomething", extensionFrame);

    // shutdown thread pool while unloading
    api.extension().registerUnloadingHandler(new UnloadHandler(pool));
  }
}

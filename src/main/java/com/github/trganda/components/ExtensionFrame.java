package com.github.trganda.components;

import burp.api.montoya.MontoyaApi;
import com.github.trganda.components.config.Config;

import javax.swing.*;
import java.awt.*;

public class ExtensionFrame extends JPanel {
    private final Dashboard dashboard;
    private final Config config;

    public ExtensionFrame() {
        this.setLayout(new BorderLayout());

        dashboard = new Dashboard();
        config = new Config();

        // main panel
        JTabbedPane mainTabs = new JTabbedPane();
        mainTabs.addTab("Dashboard", dashboard);
        mainTabs.addTab("Config", config);

        this.add(mainTabs);
    }

    public InformationPane getFilterSplitPane() {
        return dashboard.getFilterSplitPane();
    }
}

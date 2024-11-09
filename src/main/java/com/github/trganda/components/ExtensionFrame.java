package com.github.trganda.components;

import com.github.trganda.components.config.ConfigPane;
import com.github.trganda.components.dashboard.Dashboard;
import java.awt.*;
import javax.swing.*;

public class ExtensionFrame extends JPanel {
  private final Dashboard dashboard;
  private final ConfigPane config;

  public ExtensionFrame() {
    this.setLayout(new BorderLayout());

    dashboard = new Dashboard();
    config = new ConfigPane();

    // dashboard
    JTabbedPane mainTabs = new JTabbedPane();
    mainTabs.addTab("Dashboard", dashboard);
    // configuration
    JScrollPane scrollPane = new JScrollPane(config);
    mainTabs.addTab("Config", scrollPane);

    this.add(mainTabs);
  }
}

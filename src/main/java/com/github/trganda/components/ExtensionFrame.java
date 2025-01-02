package com.github.trganda.components;

import com.github.trganda.components.config.ConfigPanel;
import com.github.trganda.components.dashboard.Dashboard;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class ExtensionFrame extends JPanel {
  private final Dashboard dashboard;
  private final ConfigPanel config;

  public ExtensionFrame() {
    this.setLayout(new BorderLayout());

    dashboard = new Dashboard();
    config = new ConfigPanel();

    // dashboard
    JTabbedPane mainTabs = new JTabbedPane();
    mainTabs.addTab("Dashboard", dashboard);
    // configuration
    JScrollPane scrollPane = new JScrollPane(config);
    mainTabs.addTab("Config", scrollPane);

    this.add(mainTabs);
  }
}

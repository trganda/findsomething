package com.github.trganda.components.config;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class ConfigPanel extends JPanel {

  private RulePanel rulePanel;
  private FilterListPane blackListPane;

  public ConfigPanel() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(gridBagLayout);
    this.setBorder(BorderFactory.createEmptyBorder(20, 20, 50, 20));

    rulePanel = new RulePanel();
    blackListPane = new FilterListPane();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weighty = 0.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.PAGE_START;
    gbc.insets = new Insets(0, 0, 20, 0);
    this.add(rulePanel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(0, 0, 0, 0);
    JSeparator separator = new JSeparator();
    this.add(separator, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.insets = new Insets(20, 0, 0, 0);
    this.add(blackListPane, gbc);
  }
}

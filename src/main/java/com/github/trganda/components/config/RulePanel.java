package com.github.trganda.components.config;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class RulePanel extends JPanel {

  private final JLabel label;
  private final RuleInnerPanel ruleInnerPanel;
  private final GroupPanel groupPanel;

  public RulePanel() {
    label = new JLabel("Rules set");
    label.setFont(new Font("Arial", Font.BOLD, 16));
    ruleInnerPanel = new RuleInnerPanel();
    groupPanel = new GroupPanel();

    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(gridBagLayout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(0, 0, 12, 5);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(label, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(0, 0, 0, 0);
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(ruleInnerPanel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(15, 0, 15, 0);
    this.add(new JSeparator(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(groupPanel, gbc);
  }
}

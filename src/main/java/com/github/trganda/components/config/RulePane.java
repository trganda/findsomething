package com.github.trganda.components.config;

import java.awt.*;
import javax.swing.*;

public class RulePane extends JPanel {

  private JLabel label;
  private JLabel description;
  private RuleInnerPane ruleInnerPane;

  public RulePane() {
    label = new JLabel("Rules set");
    label.setFont(new Font("Arial", Font.BOLD, 16));
    description = new JLabel("Defining rules to extract information that you are looking for.");
    ruleInnerPane = new RuleInnerPane();

    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(gridBagLayout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(0, 0, 12, 5);
    gbc.anchor = GridBagConstraints.NORTHWEST;
    this.add(label, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    this.add(description, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(0, 0, 0, 0);
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(ruleInnerPane, gbc);

    // Config.getInstance().registerConfigListener(ruleInnerPane);
  }
}

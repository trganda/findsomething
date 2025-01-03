package com.github.trganda.components.dashboard;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class StatusPanel extends JPanel {

  private final JLabel countLabel;
  private final JLabel groupLabel;
  private final JLabel info;
  private final JLabel infoLabel;

  public StatusPanel() {
    countLabel = new JLabel("0");
    groupLabel = new JLabel("General");
    info = new JLabel("");
    infoLabel = new JLabel("");
    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.insets = new Insets(0, 0, 0, 5);
    this.add(info, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 0, 5);
    gbc.anchor = GridBagConstraints.LINE_START;
    this.add(infoLabel, gbc);

    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.insets = new Insets(0, 0, 0, 5);
    this.add(new JLabel("Count:"), gbc);

    gbc.gridx = 3;
    gbc.gridy = 0;
    this.add(countLabel, gbc);

    gbc.gridx = 4;
    gbc.gridy = 0;
    this.add(new JLabel("Group:"), gbc);

    gbc.gridx = 5;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(groupLabel, gbc);
  }
}

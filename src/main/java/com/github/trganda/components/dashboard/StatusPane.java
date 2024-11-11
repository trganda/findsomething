package com.github.trganda.components.dashboard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import lombok.Getter;

@Getter
public class StatusPane extends JPanel {

  private JLabel countLabel;

  private JLabel groupLabel;

  public StatusPane() {
    countLabel = new JLabel("0");
    groupLabel = new JLabel("0");

    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 0, 5);
    gbc.anchor = GridBagConstraints.LINE_END;
    this.add(new JLabel("Count:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    this.add(countLabel, gbc);

    gbc.gridx = 2;
    gbc.gridy = 0;
    this.add(new JLabel("in Group:"), gbc);

    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(groupLabel, gbc);
  }
}

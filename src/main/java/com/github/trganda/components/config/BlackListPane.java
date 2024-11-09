package com.github.trganda.components.config;

import java.awt.*;
import javax.swing.*;

import lombok.Getter;

@Getter
public class BlackListPane extends JPanel {

  private BlackListInnerPane blackListInnerPane;

  public BlackListPane() {
    blackListInnerPane = new BlackListInnerPane();
    JLabel label = new JLabel("Filter setting");
    label.setFont(new Font("Arial", Font.BOLD, 16));
    JLabel description =
        new JLabel(
            "You can set different type black list to ignore while grep for" + " information.");

    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(gridBagLayout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 12, 0);
    gbc.anchor = GridBagConstraints.LINE_START;
    this.add(label, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    this.add(description, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(blackListInnerPane, gbc);

    // Config.getInstance().registerConfigListener(blackListInnerPane);
  }
}

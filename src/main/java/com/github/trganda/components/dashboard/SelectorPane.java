package com.github.trganda.components.dashboard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import lombok.Getter;

@Deprecated
@Getter
public class SelectorPane extends JPanel {
  private JTextField hosTextField;
  private JTextField statusTextField;

  public SelectorPane() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupComponents() {
    hosTextField = new JTextField();
    statusTextField = new JTextField();
  }

  private void setupLayout() {
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.LINE_START;
    this.add(new JLabel("Filter"), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 0, 10, 0);
    this.add(new JSeparator(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 0.0;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(0, 0, 5, 5);
    this.add(new JLabel("Host:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.insets = new Insets(0, 0, 5, 0);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(hosTextField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(0, 0, 5, 5);
    this.add(new JLabel("Status:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.insets = new Insets(0, 0, 5, 0);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(statusTextField, gbc);
  }
}

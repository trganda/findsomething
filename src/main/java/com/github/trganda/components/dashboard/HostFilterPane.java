package com.github.trganda.components.dashboard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import lombok.Getter;

@Getter
public class HostFilterPane extends JPanel {
  private JTextField hosTextField;
  private JTextField statusTextField;

  public HostFilterPane() {
    this.setupComponents();
    this.setupLayout();
    this.setBorder(new TitledBorder("Filter by host"));
  }

  private void setupComponents() {
    hosTextField = new JTextField();
    hosTextField.setPreferredSize(new Dimension(200, hosTextField.getPreferredSize().height));

    statusTextField = new JTextField();
    statusTextField.setPreferredSize(new Dimension(200, statusTextField.getPreferredSize().height));
  }

  private void setupLayout() {
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.insets = new Insets(0, 2, 5, 5);
    this.add(new JLabel("Host:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 5, 2);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(hosTextField, gbc);

    // gbc.gridx = 0;
    // gbc.gridy = 1;
    // gbc.fill = GridBagConstraints.NONE;
    // gbc.insets = new Insets(0, 0, 5, 5);
    // this.add(new JLabel("Status:"), gbc);

    // gbc.gridx = 1;
    // gbc.gridy = 1;
    // gbc.insets = new Insets(0, 0, 5, 0);
    // gbc.fill = GridBagConstraints.HORIZONTAL;
    // this.add(statusTextField, gbc);
  }
}

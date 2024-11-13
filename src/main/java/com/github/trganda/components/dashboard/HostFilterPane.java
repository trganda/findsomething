package com.github.trganda.components.dashboard;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HostFilterPane extends JPanel {
  private JTextField hostFilterField;

  public HostFilterPane() {
    hostFilterField = new JTextField();
    hostFilterField.setMinimumSize(new Dimension(200, hostFilterField.getPreferredSize().height));
    hostFilterField.setPreferredSize(new Dimension(800, hostFilterField.getPreferredSize().height));
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    this.add(new JLabel("Host:"));
    this.add(Box.createHorizontalStrut(5));
    this.add(hostFilterField);
  }
}

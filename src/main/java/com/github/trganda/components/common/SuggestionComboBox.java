package com.github.trganda.components.common;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SuggestionComboBox extends JPanel {
  private JComboBox<String> hostComboBox;
  private DefaultComboBoxModel<String> hostComboBoxModel;
  private JTextField hostTextField;
  // Identify the user are typing for match a host (true) or not (false)
  private boolean matching;
  private boolean matched;
  private static final String placeHolder = "Enter a host";

  public SuggestionComboBox() {
    this.hostComboBoxModel = new DefaultComboBoxModel<>();
    this.hostComboBox = new JComboBox<>(hostComboBoxModel);
    this.hostTextField = new JTextField();
    this.matching = false;
    this.matched = false;

    this.hostComboBox.setMaximumRowCount(6);
    this.hostComboBox.setPreferredSize(new Dimension(200, hostComboBox.getPreferredSize().height));
    this.hostTextField.setPreferredSize(
        new Dimension(200, hostTextField.getPreferredSize().height));

    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    this.setLayout(layout);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(hostTextField, gbc);
    this.add(hostComboBox, gbc);

    this.setupEvent();
  }

  public String getSelectedHost() {
    if (!this.isMatching() && hostComboBox.getSelectedIndex() >= 0) {
      return hostComboBox.getSelectedItem().toString();
    }
    return "*";
  }

  private void setupEvent() {
    hostTextField.getDocument().addDocumentListener(new SuggestionDocumentListener(this));
    hostTextField.addKeyListener(new SuggestionKeyListener(this));
    hostComboBox.addActionListener(
        e -> {
          if (!this.isMatching() && hostComboBox.getSelectedIndex() >= 0) {
            String selectedHost = hostComboBox.getSelectedItem().toString();
            matched = true;
            hostTextField.setText(selectedHost);
            hostComboBox.setPopupVisible(false);
            matched = false;
          }
        });
  }
}

package com.github.trganda.components.common;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SuggestionComboBox extends JPanel {
  private JComboBox<String> hostComboBox;
  private DefaultComboBoxModel<String> hostComboBoxModel;
  private PlaceHolderTextField hostTextField;
  // Identify the user are typing for match a host (true) or not (false)
  private boolean matching;
  private static final String placeHolder = "Enter a host";

  public SuggestionComboBox() {
    this.hostComboBoxModel = new DefaultComboBoxModel<>();
    this.hostComboBox = new JComboBox<String>(hostComboBoxModel);
    this.hostTextField = new PlaceHolderTextField(placeHolder);
    this.matching = false;

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
  }
}

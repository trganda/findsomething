package com.github.trganda.components.dashboard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import lombok.Getter;

@Getter
public class HostFilterPane extends JPanel {
  private JComboBox<String> hostComboBox;
  private DefaultComboBoxModel<String> hostComboBoxModel;
  // private JTextField statusTextField;

  public HostFilterPane() {
    this.setupComponents();
    this.setupLayout();
    this.setBorder(new TitledBorder("Filter by host"));
  }

  private void setupComponents() {
    hostComboBoxModel = new DefaultComboBoxModel<>();
    hostComboBox = new JComboBox<>(hostComboBoxModel);
    hostComboBox.setEditable(true);
    // hidden the button and remove it from the combobox
    hostComboBox.setUI(new BasicComboBoxUI(){
      @Override
      protected JButton createArrowButton() {
        return new JButton() {
          @Override
          public int getWidth() {
              return 0;
          }
      };
      }
    });
    hostComboBox.remove(hostComboBox.getComponent(0));
    hostComboBox.setPreferredSize(new Dimension(200, hostComboBox.getPreferredSize().height));

    // statusTextField = new JTextField();
    // statusTextField.setPreferredSize(new Dimension(200, statusTextField.getPreferredSize().height));
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
    this.add(hostComboBox, gbc);

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

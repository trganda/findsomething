package com.github.trganda.components.dashboard.filter;

import com.github.trganda.components.common.PlaceHolderTextField;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import lombok.Getter;

@Getter
public class InformationFilterPane extends JPanel {
  private final String filterPlaceHolder = "Search";
  private PlaceHolderTextField filterField;
  private JCheckBox sensitive;
  private JCheckBox negative;

  public InformationFilterPane() {
    this.setupComponents();
    this.setupLayout();
    this.setBorder(new TitledBorder("Filter by search term"));
  }

  private void setupComponents() {
    filterField = new PlaceHolderTextField(filterPlaceHolder);
    filterField.setPreferredSize(new Dimension(200, filterField.getPreferredSize().height));
    sensitive = new JCheckBox("Case sensitive");
    negative = new JCheckBox("Negative search");
  }

  private void setupLayout() {
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 5, 2);
    this.add(filterField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(0, 2, 5, 5);
    this.add(sensitive, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.insets = new Insets(0, 2, 5, 2);
    this.add(negative, gbc);
  }
}

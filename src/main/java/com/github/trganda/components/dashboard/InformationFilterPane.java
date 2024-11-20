package com.github.trganda.components.dashboard;

import static com.github.trganda.config.Config.GROUP_FINGERPRINT;
import static com.github.trganda.config.Config.GROUP_GENERAL;
import static com.github.trganda.config.Config.GROUP_INFORMATION;
import static com.github.trganda.config.Config.GROUP_SENSITIVE;
import static com.github.trganda.config.Config.GROUP_VULNERABILITY;

import com.github.trganda.components.common.PlaceHolderTextField;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import lombok.Getter;

@Getter
public class InformationFilterPane extends JPanel {
  private final String filterPlaceHolder = "Search";
  // private JComboBox<String> selector;
  private PlaceHolderTextField filterField;
  private JCheckBox sensitive;

  public InformationFilterPane() {
    this.setupComponents();
    this.setupLayout();
    this.setBorder(new TitledBorder("Filter"));
  }

  private void setupComponents() {
    // selector =
    //     new JComboBox<>(
    //         new String[] {
    //           GROUP_GENERAL,
    //           GROUP_FINGERPRINT,
    //           GROUP_SENSITIVE,
    //           GROUP_VULNERABILITY,
    //           GROUP_INFORMATION
    //         });
    filterField = new PlaceHolderTextField(filterPlaceHolder);
    filterField.setPreferredSize(new Dimension(200, filterField.getPreferredSize().height));
    sensitive = new JCheckBox();
  }

  private void setupLayout() {
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 2, 5, 5);
    this.add(new JLabel("Search:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 5, 2);
    this.add(filterField, gbc);

    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 2, 5, 5);
    this.add(sensitive, gbc);

    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 5, 2);
    this.add(new JLabel("Case sensitive"), gbc);

    // gbc.gridx = 0;
    // gbc.gridy = 1;
    // gbc.insets = new Insets(0, 2, 5, 5);
    // this.add(new JLabel("Group:"), gbc);

    // gbc.gridx = 1;
    // gbc.gridy = 1;
    // gbc.insets = new Insets(0, 0, 5, 2);
    // this.add(selector, gbc);
  }
}

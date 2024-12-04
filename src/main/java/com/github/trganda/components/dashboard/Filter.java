package com.github.trganda.components.dashboard;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class Filter extends JDialog {

  private final HostFilterPane hostFilter;
  private final InformationFilterPane informationFilter;
  private final FilterButtonPanel filterButtonPanel;
  private final JPanel innerPanel;

  public Filter(Frame pFrame) {
    super(pFrame, "Filter", true);
    this.hostFilter = new HostFilterPane();
    this.informationFilter = new InformationFilterPane();
    this.filterButtonPanel = new FilterButtonPanel();
    this.innerPanel = new JPanel();

    this.setupLayout();
  }

  public void setupLayout() {
    this.setLayout(new BorderLayout());
    this.add(innerPanel);
    this.innerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    innerPanel.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.insets = new Insets(0, 0, 0, 5);
    innerPanel.add(hostFilter, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 0, 0);
    innerPanel.add(informationFilter, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.insets = new Insets(15, 0, 0, 0);
    innerPanel.add(filterButtonPanel, gbc);
  }

  public String getHost() {
    return this.hostFilter.getSuggestion().getSelectedHost();
  }

  public String getRuleType() {
    return hostFilter.getSelector().getSelectedItem().toString();
  }

  public String getSearchTerm() {
    return informationFilter.getFilterField().getText();
  }

  public boolean isNegative() {
    return informationFilter.getNegative().isSelected();
  }

  public boolean isSensitive() {
    return informationFilter.getSensitive().isSelected();
  }
}

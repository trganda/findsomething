package com.github.trganda.components.dashboard.filter;

import com.github.trganda.model.Filter;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class FilterEditor extends JDialog {

  private final HostFilterPane hostFilter;
  private final InformationFilterPane informationFilter;
  private final FilterButtonPanel filterButtonPanel;
  private final JPanel innerPanel;
  private Filter filter;

  public FilterEditor(Frame pFrame) {
    super(pFrame, "Filter");
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

    GridBagConstraints gbc = new GridBagConstraints();
    innerPanel.setLayout(new GridBagLayout());

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

  public Filter getFilter() {
    return Filter.builder()
        .host(this.getHost())
        .group(this.getRuleType())
        .searchTerm(this.getSearchTerm())
        .negative(this.isNegative())
        .sensitive(this.isSensitive())
        .build();
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
    if (!filter.getHost().isEmpty()) {
      this.hostFilter.getSuggestion().setMatched(true);
      this.hostFilter.getSuggestion().getHostTextField().setText(filter.getHost());
      this.hostFilter.getSuggestion().getHostComboBox().setPopupVisible(false);
      this.hostFilter.getSuggestion().setMatched(false);
    }
    this.hostFilter.getSelector().setSelectedItem(filter.getGroup());
    if (!filter.getSearchTerm().isEmpty()) {
      this.informationFilter.getFilterField().setText(filter.getSearchTerm());
    }
    this.informationFilter.getNegative().setSelected(filter.isNegative());
    this.informationFilter.getSensitive().setSelected(filter.isSensitive());
  }

}

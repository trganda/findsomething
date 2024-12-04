package com.github.trganda.controller.dashboard;

import com.github.trganda.FindSomething;
import com.github.trganda.components.common.PlaceHolderTextField;
import com.github.trganda.components.dashboard.filter.Filter;
import com.github.trganda.components.dashboard.filter.FilterButtonPanel;
import com.github.trganda.handler.FilterChangeListener;
import com.github.trganda.model.FilterModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class InfoFilterController {

  private final JButton filterButton;
  private final Filter filter;
  private final FilterButtonPanel filterButtonPanel;
  private List<FilterChangeListener> listeners = new ArrayList<>();
  private InfoController infoController;
  private FilterModel currentFilter;

  public InfoFilterController(JButton filterButton, InfoController infoController) {
    this.filterButton = filterButton;
    this.infoController = infoController;

    Frame pFrame = FindSomething.API.userInterface().swingUtils().suiteFrame();
    this.filter = new Filter(pFrame);
    this.filter.setLocationRelativeTo(pFrame);
    this.filterButtonPanel = this.filter.getFilterButtonPanel();
    this.setupEventListener();
  }

  public void registerFilterChangeListener(FilterChangeListener listener) {
    this.listeners.add(listener);
  }

  private void setupEventListener() {
    this.filterButton.addActionListener(
        e -> {
          currentFilter = FilterModel.getFilterModel();
          filter.pack();
          filter.setVisible(true);
        });
    this.filterButtonPanel.getCancel().addActionListener(e -> this.filter.setVisible(false));
    this.filterButtonPanel
        .getApply()
        .addActionListener(
            e -> {
              updateFilter();
              List<String> modifiedFields = currentFilter.getModifiedFields(FilterModel.getFilterModel());
              if (modifiedFields.isEmpty()) {
                return;
              }
              FilterModel filterModel = FilterModel.getFilterModel();
              PlaceHolderTextField filterField = this.filter.getInformationFilter().getFilterField();
              infoController.updateFilter(filterModel.getSearchTerm(), filterModel.isSensitive(), filterModel.isNegative(), filterField.isPlaceholderActive());
              infoController.updateActiveInfoView();
            });
    this.filterButtonPanel
        .getApplyClose()
        .addActionListener(
            e -> {
              FilterModel filterModel = FilterModel.getFilterModel();
              PlaceHolderTextField filterField = this.filter.getInformationFilter().getFilterField();
              infoController.updateFilter(filterModel.getSearchTerm(), filterModel.isSensitive(), filterModel.isNegative(), filterField.isPlaceholderActive());
              infoController.updateActiveInfoView();
              updateFilter();
              this.filter.setVisible(false);
            });
  }

  private void notifyListeners() {
    for (FilterChangeListener listener : listeners) {
      listener.onFilterChanged();
    }
  }

  private void updateFilter() {
    FilterModel.getFilterModel().setHost(this.filter.getHost());
    FilterModel.getFilterModel().setRuleType(this.filter.getRuleType());
    FilterModel.getFilterModel().setSearchTerm(this.filter.getSearchTerm());
    FilterModel.getFilterModel().setSensitive(this.filter.isSensitive());
    FilterModel.getFilterModel().setNegative(this.filter.isNegative());
  }
}

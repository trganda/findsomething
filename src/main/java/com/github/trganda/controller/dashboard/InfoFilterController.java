package com.github.trganda.controller.dashboard;

import com.github.trganda.FindSomething;
import com.github.trganda.components.dashboard.filter.FilterButtonPanel;
import com.github.trganda.components.dashboard.filter.FilterEditor;
import com.github.trganda.handler.FilterChangeListener;
import com.github.trganda.model.Filter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class InfoFilterController {

  private final JButton filterButton;
  private final FilterEditor filterEditor;
  private final FilterButtonPanel filterButtonPanel;
  private List<FilterChangeListener> listeners = new ArrayList<>();
  private InfoController infoController;
  private Filter prevFilter;

  private JTextField filterField;

  public InfoFilterController(JButton filterButton, InfoController infoController) {
    this.filterButton = filterButton;
    this.infoController = infoController;

    Frame pFrame = FindSomething.API.userInterface().swingUtils().suiteFrame();
    this.filterEditor = new FilterEditor(pFrame);
    this.filterEditor.setLocationRelativeTo(pFrame);
    this.filterButtonPanel = this.filterEditor.getFilterButtonPanel();
    this.filterField = this.filterEditor.getInformationFilter().getFilterField();
    this.setupEventListener();
  }

  public void registerFilterChangeListener(FilterChangeListener listener) {
    this.listeners.add(listener);
  }

  private void setupEventListener() {
    this.filterButton.addActionListener(
        e -> {
          prevFilter = Filter.getFilter();
          filterEditor.pack();
          filterEditor.setVisible(true);
          filterEditor.setFilter(prevFilter);
        });
    this.filterButtonPanel
        .getCancel()
        .addActionListener(
            e -> {
              this.filterEditor.setVisible(false);
              this.filterEditor.setFilter(prevFilter);
            });
    this.filterButtonPanel
        .getApply()
        .addActionListener(
            e -> {
              Filter currentFilter = this.filterEditor.getFilter();
              List<String> modifiedFields = prevFilter.getModifiedFields(currentFilter);
              if (modifiedFields.isEmpty()) {
                return;
              }

              Filter.getFilter().update(currentFilter);
              modifiedFields.forEach(
                  f -> {
                    if (f.equals("1")) {
                      infoController.updateInfoView(currentFilter, false);
                    } else if (f.equals("2")) {
                      infoController.updateTableFilter(
                          currentFilter.getSearchTerm(),
                          currentFilter.isSensitive(),
                          currentFilter.isNegative(),
                          false);
                    }
                  });
            });
    this.filterButtonPanel
        .getApplyClose()
        .addActionListener(
            e -> {
              this.filterEditor.setVisible(false);
              this.filterEditor.setFilter(prevFilter);
            });
  }

  private void notifyListeners() {
    for (FilterChangeListener listener : listeners) {
      listener.onFilterChanged();
    }
  }
}

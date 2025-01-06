package com.github.trganda.controller.config;

import com.github.trganda.components.config.GroupPanel;
import com.github.trganda.config.ConfigChangeListener;
import com.github.trganda.config.ConfigManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

public class GroupController implements ConfigChangeListener {

  private final GroupPanel groupPanel;

  public GroupController(GroupPanel groupPanel) {
    this.groupPanel = groupPanel;
    ConfigManager.getInstance().registerConfigListener(this);
    this.loadDefaultData();
  }

  private void setupEvent() {
    this.groupPanel
        .getGroupButtonsPanel()
        .getRemove()
        .addActionListener(
            e -> {
              int selectedRow = this.groupPanel.getRuleGroupTable().getSelectedRow();
              if (selectedRow >= 0) {
                this.groupPanel.getRuleGroupTableModel().removeRow(selectedRow);
              }
            });

    this.groupPanel
        .getAdd()
        .addActionListener(
            e -> {
              String val = groupPanel.getAddField().getText();
              if (val.isEmpty()) {
                return;
              }
              // ignore if already exists same value
              if (groupPanel.getRuleGroupTableModel().getDataVector().stream()
                  .anyMatch(row -> row.get(0).equals(val))) {
                return;
              }
            });
  }

  private void loadDefaultData() {
    this.onConfigChange(ConfigManager.getInstance());
  }

  public void onConfigChange(ConfigManager configManager) {
    SwingWorker<List<Object[]>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Object[]> doInBackground() throws Exception {
            List<Object[]> list = new ArrayList<>();
            configManager
                .getRules()
                .getGroups()
                .forEach(group -> list.add(new Object[] {group.getGroup()}));
            return list;
          }

          @Override
          protected void done() {
            try {
              List<Object[]> result = get();
              groupPanel.getRuleGroupTableModel().setRowCount(0);
              result.forEach(row -> groupPanel.getRuleGroupTableModel().addRow(row));
              groupPanel.getRuleGroupTableModel().fireTableDataChanged();
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }
        };

    worker.execute();
  }
}

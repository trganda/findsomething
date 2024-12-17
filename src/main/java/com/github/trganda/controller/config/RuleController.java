package com.github.trganda.controller.config;

import com.github.trganda.FindSomething;
import com.github.trganda.components.config.RuleInnerPane;
import com.github.trganda.config.ConfigChangeListener;
import com.github.trganda.config.ConfigManager;
import com.github.trganda.config.Operation;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.model.RuleModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class RuleController implements ConfigChangeListener {

  private RuleInnerPane innerPane;
  private List<Rule> rules;
  private RuleEditorController editorController;

  public RuleController() {
    ConfigManager.getInstance().registerConfigListener(this);
  }

  public RuleController(RuleInnerPane innerPane, RuleEditorController editorController) {
    this();
    this.innerPane = innerPane;
    this.editorController = editorController;
    this.setupEventListener();
    this.loadDefaultRules();
  }

  private void setupEventListener() {
    // selector event
    this.innerPane
        .getSelector()
        .addActionListener(
            e -> {
              this.onConfigChange(ConfigManager.getInstance());
            });

    this.innerPane
        .getRuleSearch()
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
              @Override
              public void insertUpdate(DocumentEvent e) {
                updateTableFilter(
                    innerPane.getRuleSearch().getText(),
                    innerPane.getRuleSearch().isPlaceholderActive());
              }

              @Override
              public void removeUpdate(DocumentEvent e) {
                updateTableFilter(
                    innerPane.getRuleSearch().getText(),
                    innerPane.getRuleSearch().isPlaceholderActive());
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
                updateTableFilter(
                    innerPane.getRuleSearch().getText(),
                    innerPane.getRuleSearch().isPlaceholderActive());
              }

              public void updateTableFilter(String filter, boolean isPlaceholderActive) {
                RowFilter<TableModel, Object> rf = null;
                if (!isPlaceholderActive) {
                  try {
                    filter = "(?i)" + filter;
                    rf = RowFilter.regexFilter(filter, 1);
                  } catch (java.util.regex.PatternSyntaxException e) {
                    return;
                  }

                  TableRowSorter<TableModel> sorter =
                      new TableRowSorter<TableModel>(innerPane.getModel());
                  sorter.setRowFilter(rf);
                  innerPane.getTable().setRowSorter(sorter);
                }
              }
            });

    // table event
    JTable ruleTable = this.innerPane.getTable();
    TableModel ruleTableModel = ruleTable.getModel();
    this.innerPane
        .getTable()
        .addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                int row = ruleTable.rowAtPoint(e.getPoint());
                int column = ruleTable.columnAtPoint(e.getPoint());
                if (row == -1 || column == -1) {
                  return;
                }

                if (ruleTable.getColumnClass(column) == Boolean.class) {
                  Boolean value = (Boolean) ruleTable.getValueAt(row, column);
                  String name = ruleTableModel.getValueAt(row, 1).toString();
                  String group = innerPane.getSelector().getSelectedItem().toString();

                  rules = ConfigManager.getInstance().getRules().getRulesWithGroup(group);
                  rules.stream()
                      .filter(r -> r.getName().equals(name))
                      .findFirst()
                      .ifPresent(
                          r -> {
                            r.setEnabled(value);
                            ConfigManager.getInstance().syncRules(group, r, Operation.ENB);
                          });
                }
              }
            });

    // button event
    this.innerPane
        .getRuleButtonsPane()
        .getAdd()
        .addActionListener(
            e -> {
              String group = this.innerPane.getSelector().getSelectedItem().toString();
              RuleModel ruleModel = new RuleModel();
              ruleModel.setGroup(group);
              ruleModel.setRule(Rule.builder().build());
              this.editorController.updateEditorAndView(ruleModel);
            });

    this.innerPane
        .getRuleButtonsPane()
        .getEdit()
        .addActionListener(
            e -> {
              String group = this.innerPane.getSelector().getSelectedItem().toString();
              showEditor(
                  r -> {
                    RuleModel ruleModel = new RuleModel();
                    ruleModel.setGroup(group);
                    ruleModel.setRule(r);
                    this.editorController.updateEditorAndView(ruleModel);
                  });
            });

    this.innerPane
        .getRuleButtonsPane()
        .getRemove()
        .addActionListener(
            e -> {
              String group = this.innerPane.getSelector().getSelectedItem().toString();
              showEditor(
                  r -> {
                    ConfigManager.getInstance().syncRules(group, r, Operation.DEL);
                  });
            });

    this.innerPane
        .getRuleButtonsPane()
        .getClear()
        .addActionListener(
            e -> {
              String group = this.innerPane.getSelector().getSelectedItem().toString();
              ConfigManager.getInstance().syncRules(group, null, Operation.CLR);
            });
  }

  private void loadDefaultRules() {
    this.onConfigChange(ConfigManager.getInstance());
  }

  @Override
  public void onConfigChange(ConfigManager configManager) {
    SwingWorker<List<Object[]>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Object[]> doInBackground() {
            List<Object[]> list = new ArrayList<>();
            String selectedItem = innerPane.getSelector().getSelectedItem().toString();
            for (Rule rule : configManager.getRules().getRulesWithGroup(selectedItem)) {
              list.add(rule.toObjectArray());
            }
            return list;
          }

          @Override
          protected void done() {
            try {
              List<Object[]> result = get();
              innerPane.getModel().setRowCount(0);
              for (Object[] row : result) {
                innerPane.getModel().addRow(row);
              }
              innerPane.getModel().fireTableDataChanged();
              innerPane.getCountLabel().setText(String.valueOf(result.size()));
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };
    worker.execute();
  }

  private void showEditor(Consumer<Rule> consumer) {
    int idx = this.innerPane.getTable().getSelectedRow();
    if (idx == -1) {
      return;
    }

    String group = this.innerPane.getSelector().getSelectedItem().toString();
    rules = ConfigManager.getInstance().getRules().getRulesWithGroup(group);
    String name = this.innerPane.getModel().getValueAt(idx, 1).toString();

    rules.stream().filter(r -> r.getName().equals(name)).findFirst().ifPresent(consumer);
  }
}

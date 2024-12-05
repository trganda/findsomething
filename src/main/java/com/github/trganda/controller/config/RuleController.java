package com.github.trganda.controller.config;

import com.github.trganda.FindSomething;
import com.github.trganda.components.config.RuleInnerPane;
import com.github.trganda.config.Config;
import com.github.trganda.config.ConfigChangeListener;
import com.github.trganda.config.Operation;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.model.RuleModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.TableModel;

public class RuleController implements ConfigChangeListener {

  private RuleInnerPane innerPane;
  private List<Rule> rules;
  private RuleEditorController editorController;

  public RuleController() {
    Config.getInstance().registerConfigListener(this);
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
              this.onConfigChange(Config.getInstance());
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

                  rules = Config.getInstance().getRules().getRulesWithGroup(group);
                  rules.stream()
                      .filter(r -> r.getName().equals(name))
                      .findFirst()
                      .ifPresent(
                          r -> {
                            r.setEnabled(value);
                            Config.getInstance().syncRules(group, r, Operation.ENB);
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
              ruleModel.setRule(new Rule());
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
                    Config.getInstance().syncRules(group, r, Operation.DEL);
                  });
            });

    this.innerPane
        .getRuleButtonsPane()
        .getClear()
        .addActionListener(
            e -> {
              String group = this.innerPane.getSelector().getSelectedItem().toString();
              Config.getInstance().syncRules(group, null, Operation.CLR);
            });
  }

  private void loadDefaultRules() {
    this.onConfigChange(Config.getInstance());
  }

  @Override
  public void onConfigChange(Config config) {
    SwingWorker<List<Object[]>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Object[]> doInBackground() {
            List<Object[]> list = new ArrayList<>();
            String selectedItem = innerPane.getSelector().getSelectedItem().toString();
            for (Rule rule : config.getRules().getRulesWithGroup(selectedItem)) {
              list.add(
                  new Object[] {
                    rule.isEnabled(),
                    rule.getName(),
                    rule.getRegex(),
                    rule.getScope(),
                    rule.isSensitive()
                  });
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
    rules = Config.getInstance().getRules().getRulesWithGroup(group);
    String name = this.innerPane.getModel().getValueAt(idx, 1).toString();

    rules.stream().filter(r -> r.getName().equals(name)).findFirst().ifPresent(consumer);
  }
}

package com.github.trganda.components.config;

import static com.github.trganda.config.Config.*;

import com.github.trganda.FindSomething;
import com.github.trganda.config.Config;
import com.github.trganda.config.ConfigChangeListener;
import com.github.trganda.config.Operatation;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.model.cache.CachePool;
import com.github.trganda.utils.Utils;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RuleInnerPane extends JPanel implements ConfigChangeListener {

  private RuleButtonsPane ruleButtonsPane;
  private JComboBox<String> selector;
  private JTable table;
  private DefaultTableModel model;
  private JComponent wrap;
  private JLabel countLabel;

  public RuleInnerPane() {
    this.setupComponents();

    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    this.add(new JLabel("Rule type:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(selector, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.insets = new Insets(5, 0, 0, 0);
    this.add(new JLabel("Rule count:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    this.add(countLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(15, 0, 20, 0);
    JSeparator separator = new JSeparator();
    this.add(separator, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.insets = new Insets(0, 0, 5, 5);
    this.add(ruleButtonsPane, gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(wrap, gbc);
  }

  private JComponent setupTable() {
    model =
        new DefaultTableModel(new Object[] {"Enabled", "Name", "Regex", "Scope", "Sensitive"}, 0) {
          @Override
          public Class<?> getColumnClass(int column) {
            // set column to using combobox
            return (column == 0) ? Boolean.class : String.class;
          }

          @Override
          public boolean isCellEditable(int row, int column) {
            return column == 0;
          }
        };
    table = new JTable(model);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setPreferredSize(new Dimension(table.getPreferredSize().width, 160));
    scrollPane.addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            resizePane();
          }
        });

    return scrollPane;
  }

  private void resizePane() {
    int width = table.getWidth();
    table.getColumnModel().getColumn(0).setPreferredWidth((int) (width * 0.1));
    table.getColumnModel().getColumn(1).setPreferredWidth((int) (width * 0.2));
    table.getColumnModel().getColumn(2).setPreferredWidth((int) (width * 0.5));
    table.getColumnModel().getColumn(3).setPreferredWidth((int) (width * 0.1));
    table.getColumnModel().getColumn(4).setPreferredWidth((int) (width * 0.1));
  }

  private void setupComponents() {
    ruleButtonsPane = new RuleButtonsPane();
    selector =
        new JComboBox<>(
            new String[] {
              GROUP_FINGERPRINT, GROUP_SENSITIVE, GROUP_VULNERABILITY, GROUP_INFORMATION
            });
    selector.addActionListener(
        e -> {
          String selectedItem = (String) selector.getSelectedItem();
          if (selectedItem == null) {
            return;
          }
          List<Rule> rules = Config.getInstance().getRules().getRulesWithGroup(selectedItem);
          countLabel.setText(String.valueOf(rules.size()));
          this.loadRulesWithGroup(rules);
        });

    wrap = this.setupTable();
    countLabel = new JLabel();

    // loading the default rules of group fingerprint
    List<Rule> rules = Config.getInstance().getRules().getRulesWithGroup(GROUP_FINGERPRINT);
    countLabel.setText(String.valueOf(rules.size()));
    this.loadRulesWithGroup(rules);
  }

  private void loadRulesWithGroup(List<Rule> rules) {
    SwingWorker<List<Object[]>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Object[]> doInBackground() {
            List<Object[]> list = new ArrayList<>();
            for (Rule rule : rules) {
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
              model.setRowCount(0);
              for (Object[] row : result) {
                model.addRow(row);
              }
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };
    worker.execute();
  }

  private class RuleButtonsPane extends JPanel {
    private JButton add;
    private JButton edit;
    private JButton remove;
    private JButton clear;

    public RuleButtonsPane() {
      add = new JButton("Add");
      edit = new JButton("Edit");
      remove = new JButton("Remove");
      clear = new JButton("Clear");

      setAlign(add, edit, remove, clear);
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      this.add(add);
      this.add(Box.createVerticalStrut(5));
      this.add(edit);
      this.add(Box.createVerticalStrut(5));
      this.add(remove);
      this.add(Box.createVerticalStrut(5));
      this.add(clear);

      this.setupButtonEventHandler();
    }

    private void setupButtonEventHandler() {
      Frame pFrame = FindSomething.API.userInterface().swingUtils().suiteFrame();
      add.addActionListener(
          e -> {
            String group = selector.getSelectedItem().toString();
            new Editor(pFrame, group).setVisible(true);
          });

      edit.addActionListener(
          e -> {
            int idx = table.getSelectedRow();
            if (idx == -1) {
              return;
            }
            List<Rule> rules =
                Config.getInstance()
                    .getRules()
                    .getRulesWithGroup(selector.getSelectedItem().toString());
            String name = model.getValueAt(idx, 1).toString();
            String group = selector.getSelectedItem().toString();
            rules.stream()
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .ifPresent(
                    r -> {
                      new Editor(pFrame, group, r).setVisible(true);
                    });
          });

      remove.addActionListener(
          e -> {
            String selectedItem = selector.getSelectedItem().toString();
            int[] idxes = table.getSelectedRows();
            for (int idx : idxes) {
              String ruleName = model.getValueAt(idx, 1).toString();
              Rule rule = CachePool.getRule(Utils.calHash(selectedItem, ruleName));
              if (rule != null) {
                Config.getInstance().syncRules(selectedItem, rule, Operatation.DEL);
              } else {
                FindSomething.API
                    .logging()
                    .logToError(
                        String.format(
                            "cannot find rule: '%s' in group: '%s'", ruleName, selectedItem));
              }
            }
          });

      clear.addActionListener(
          e -> {
            String selectedItem = selector.getSelectedItem().toString();
            Config.getInstance().syncRules(selectedItem, null, Operatation.CLR);
          });
    }

    private void setAlign(JButton... buttons) {
      for (var button : buttons) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
      }
    }
  }

  @Override
  public void onConfigChange(Config config) {
    SwingWorker<List<Object[]>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Object[]> doInBackground() {
            List<Object[]> list = new ArrayList<>();
            String selectedItem = selector.getSelectedItem().toString();
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
              model.setRowCount(0);
              for (Object[] row : result) {
                model.addRow(row);
              }
              model.fireTableDataChanged();
              countLabel.setText(String.valueOf(result.size()));
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };
    worker.execute();
  }
}

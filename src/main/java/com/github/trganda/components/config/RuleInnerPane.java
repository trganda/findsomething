package com.github.trganda.components.config;

import static com.github.trganda.config.Config.*;

import com.github.trganda.config.Config;
import com.github.trganda.config.Rules.Rule;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import lombok.Getter;

@Getter
public class RuleInnerPane extends JPanel {

  private RuleInnerButtonsPane ruleButtonsPane;
  private JComboBox<String> selector;
  private JTable table;
  private DefaultTableModel model;
  private JComponent wrap;
  private JLabel countLabel;

  public RuleInnerPane() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupLayout() {
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
    ruleButtonsPane = new RuleInnerButtonsPane();
    selector =
        new JComboBox<>(
            new String[] {
              GROUP_FINGERPRINT, GROUP_SENSITIVE, GROUP_VULNERABILITY, GROUP_INFORMATION
            });
    // selector.addActionListener(
    //     e -> {
    //       String selectedItem = (String) selector.getSelectedItem();
    //       if (selectedItem == null) {
    //         return;
    //       }
    //       List<Rule> rules = Config.getInstance().getRules().getRulesWithGroup(selectedItem);
    //       countLabel.setText(String.valueOf(rules.size()));
    //       this.loadRulesWithGroup(rules);
    //     });

    wrap = this.setupTable();
    countLabel = new JLabel();

    // loading the default rules of group fingerprint
    List<Rule> rules = Config.getInstance().getRules().getRulesWithGroup(GROUP_FINGERPRINT);
    countLabel.setText(String.valueOf(rules.size()));
    // this.loadRulesWithGroup(rules);
  }

  // private void loadRulesWithGroup(List<Rule> rules) {
  //   SwingWorker<List<Object[]>, Void> worker =
  //       new SwingWorker<>() {
  //         @Override
  //         protected List<Object[]> doInBackground() {
  //           List<Object[]> list = new ArrayList<>();
  //           for (Rule rule : rules) {
  //             list.add(
  //                 new Object[] {
  //                   rule.isEnabled(),
  //                   rule.getName(),
  //                   rule.getRegex(),
  //                   rule.getScope(),
  //                   rule.isSensitive()
  //                 });
  //           }
  //           return list;
  //         }

  //         @Override
  //         protected void done() {
  //           try {
  //             List<Object[]> result = get();
  //             model.setRowCount(0);
  //             for (Object[] row : result) {
  //               model.addRow(row);
  //             }
  //           } catch (InterruptedException | ExecutionException e) {
  //             FindSomething.API.logging().logToError(new RuntimeException(e));
  //           }
  //         }
  //       };
  //   worker.execute();
  // }

  // @Override
  // public void onConfigChange(Config config) {
  //   SwingWorker<List<Object[]>, Void> worker =
  //       new SwingWorker<>() {
  //         @Override
  //         protected List<Object[]> doInBackground() {
  //           List<Object[]> list = new ArrayList<>();
  //           String selectedItem = selector.getSelectedItem().toString();
  //           for (Rule rule : config.getRules().getRulesWithGroup(selectedItem)) {
  //             list.add(
  //                 new Object[] {
  //                   rule.isEnabled(),
  //                   rule.getName(),
  //                   rule.getRegex(),
  //                   rule.getScope(),
  //                   rule.isSensitive()
  //                 });
  //           }
  //           return list;
  //         }

  //         @Override
  //         protected void done() {
  //           try {
  //             List<Object[]> result = get();
  //             model.setRowCount(0);
  //             for (Object[] row : result) {
  //               model.addRow(row);
  //             }
  //             model.fireTableDataChanged();
  //             countLabel.setText(String.valueOf(result.size()));
  //           } catch (InterruptedException | ExecutionException e) {
  //             FindSomething.API.logging().logToError(new RuntimeException(e));
  //           }
  //         }
  //       };
  //   worker.execute();
  // }
}

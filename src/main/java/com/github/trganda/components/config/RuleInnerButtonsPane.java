package com.github.trganda.components.config;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import lombok.Getter;

@Getter
public class RuleInnerButtonsPane extends JPanel {
  private JButton add;
  private JButton edit;
  private JButton remove;
  private JButton clear;

  public RuleInnerButtonsPane() {
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

    // this.setupButtonEventHandler();
  }

  private void setAlign(JButton... buttons) {
    for (var button : buttons) {
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
    }
  }

  // private void setupButtonEventHandler() {
  //   Frame pFrame = FindSomething.API.userInterface().swingUtils().suiteFrame();
  //   add.addActionListener(
  //       e -> {
  //         String group = selector.getSelectedItem().toString();
  //         new Editor(pFrame, group).setVisible(true);
  //       });

  //   edit.addActionListener(
  //       e -> {
  //         int idx = table.getSelectedRow();
  //         if (idx == -1) {
  //           return;
  //         }
  //         List<Rule> rules =
  //             Config.getInstance()
  //                 .getRules()
  //                 .getRulesWithGroup(selector.getSelectedItem().toString());
  //         String name = model.getValueAt(idx, 1).toString();
  //         String group = selector.getSelectedItem().toString();
  //         rules.stream()
  //             .filter(r -> r.getName().equals(name))
  //             .findFirst()
  //             .ifPresent(
  //                 r -> {
  //                   new Editor(pFrame, group, r).setVisible(true);
  //                 });
  //       });

  //   remove.addActionListener(
  //       e -> {
  //         String selectedItem = selector.getSelectedItem().toString();
  //         int[] idxes = table.getSelectedRows();
  //         for (int idx : idxes) {
  //           String ruleName = model.getValueAt(idx, 1).toString();
  //           Rule rule = CachePool.getInstance().getRule(Utils.calHash(selectedItem, ruleName));
  //           if (rule != null) {
  //             Config.getInstance().syncRules(selectedItem, rule, Operatation.DEL);
  //           } else {
  //             FindSomething.API
  //                 .logging()
  //                 .logToError(
  //                     String.format(
  //                         "cannot find rule: '%s' in group: '%s'", ruleName, selectedItem));
  //           }
  //         }
  //       });

  //   clear.addActionListener(
  //       e -> {
  //         String selectedItem = selector.getSelectedItem().toString();
  //         Config.getInstance().syncRules(selectedItem, null, Operatation.CLR);
  //       });
  // }
}

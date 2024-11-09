package com.github.trganda.controller.config;

import static com.github.trganda.config.Config.GROUP_FINGERPRINT;

import com.github.trganda.FindSomething;
import com.github.trganda.components.config.RuleInnerPane;
import com.github.trganda.config.Config;
import com.github.trganda.config.Operatation;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.controller.Mediator;
import com.github.trganda.model.RuleModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class RuleController {

  private RuleInnerPane innerPane;
  private List<Rule> rules;
  private Mediator mediator;

  public RuleController(RuleInnerPane innerPane) {
    this.innerPane = innerPane;
    this.rules = Config.getInstance().getRules().getRulesWithGroup(GROUP_FINGERPRINT);

    this.setupEvent();
    this.loadDefaultRules();
  }

  public RuleController(RuleInnerPane innerPane, Mediator mediator) {
    this(innerPane);
    this.mediator = mediator;
  }

  private void setupEvent() {
    // selector event
    this.innerPane
        .getSelector()
        .addActionListener(
            e -> {
              rules =
                  Config.getInstance()
                      .getRules()
                      .getRulesWithGroup(this.innerPane.getSelector().getSelectedItem().toString());
              this.loadRulesWithGroup(rules);
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
              this.mediator.updateEditor(ruleModel);
            });

    this.innerPane
        .getRuleButtonsPane()
        .getEdit()
        .addActionListener(
            e -> {
              int idx = this.innerPane.getTable().getSelectedRow();
              if (idx == -1) {
                return;
              }
              rules =
                  Config.getInstance()
                      .getRules()
                      .getRulesWithGroup(this.innerPane.getSelector().getSelectedItem().toString());
              String name = this.innerPane.getModel().getValueAt(idx, 1).toString();
              String group = this.innerPane.getSelector().getSelectedItem().toString();
              rules.stream()
                  .filter(r -> r.getName().equals(name))
                  .findFirst()
                  .ifPresent(
                      r -> {
                        RuleModel ruleModel = new RuleModel();
                        ruleModel.setGroup(group);
                        ruleModel.setRule(r);
                        this.mediator.updateEditor(ruleModel);
                      });
            });

    this.innerPane
        .getRuleButtonsPane()
        .getRemove()
        .addActionListener(
            e -> {
              int idx = this.innerPane.getTable().getSelectedRow();
              if (idx == -1) {
                return;
              }
              rules =
                  Config.getInstance()
                      .getRules()
                      .getRulesWithGroup(this.innerPane.getSelector().getSelectedItem().toString());
              String name = this.innerPane.getModel().getValueAt(idx, 1).toString();
              String group = this.innerPane.getSelector().getSelectedItem().toString();
              rules.stream()
                  .filter(r -> r.getName().equals(name))
                  .findFirst()
                  .ifPresent(
                      r -> {
                        Config.getInstance().syncRules(group, r, Operatation.DEL);
                        rules.remove(r);
                      });
            });

    this.innerPane
        .getRuleButtonsPane()
        .getClear()
        .addActionListener(
            e -> {
              String group = this.innerPane.getSelector().getSelectedItem().toString();
              Config.getInstance().syncRules(group, null, Operatation.CLR);
              rules.clear();
            });
  }

  private void loadDefaultRules() {
    this.loadRulesWithGroup(rules);
    this.innerPane.getCountLabel().setText(String.valueOf(rules.size()));
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
              innerPane.getModel().setRowCount(0);
              for (Object[] row : result) {
                innerPane.getModel().addRow(row);
              }
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };
    worker.execute();
  }
}

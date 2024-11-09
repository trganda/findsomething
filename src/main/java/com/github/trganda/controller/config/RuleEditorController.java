package com.github.trganda.controller.config;

import com.github.trganda.components.config.Editor;
import com.github.trganda.config.Config;
import com.github.trganda.config.Operatation;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.config.Scope;
import com.github.trganda.controller.Mediator;
import com.github.trganda.model.RuleModel;

public class RuleEditorController {

  private Editor editor;
  private RuleModel rule;
  private Mediator mediator;

  public RuleEditorController(Editor editor, RuleModel rule) {
    this.editor = editor;
    this.rule = rule;

    this.setupEvent();
  }

  public RuleEditorController(Editor editor, RuleModel rule, Mediator mediator) {
    this(editor, rule);
    this.mediator = mediator;
    this.mediator.registerRuleEditorController(this);
  }

  private void setupEvent() {
    this.editor
        .getEditroButtonsPane()
        .getCancel()
        .addActionListener(
            e -> {
              this.editor.setVisible(false);
            });

    this.editor
        .getEditroButtonsPane()
        .getSave()
        .addActionListener(
            e -> {
              Rule r =
                  new Rule(
                      true,
                      this.editor.getNameField().getText(),
                      this.editor.getRegexField().getText(),
                      (Scope) this.editor.getScope().getSelectedItem(),
                      this.editor.getSensitive().isSelected());
              if (this.editor.getOp() == Operatation.ADD) {
                Config.getInstance().syncRules(rule.getGroup(), r, this.editor.getOp());
              } else if (this.editor.getOp() == Operatation.EDT) {
                // remove the old rule first
                Config.getInstance().syncRules(rule.getGroup(), rule.getRule(), Operatation.DEL);
                Config.getInstance().syncRules(rule.getGroup(), r, this.editor.getOp());
              }
              this.editor.setVisible(false);
            });
  }

  public void updateRule(RuleModel rule) {
    this.rule = rule;
    this.editor.getNameField().setText(rule.getRule().getName());
    this.editor.getRegexField().setText(rule.getRule().getRegex());
    this.editor.getScope().setSelectedItem(rule.getRule().getScope());
    this.editor.getSensitive().setSelected(rule.getRule().isSensitive());
    this.editor.setOp(Operatation.EDT);
    this.editor.setVisible(true);
  }
}

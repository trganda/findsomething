package com.github.trganda.controller.config;

import com.github.trganda.FindSomething;
import com.github.trganda.components.config.Editor;
import com.github.trganda.config.Config;
import com.github.trganda.config.Operation;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.config.Scope;
import com.github.trganda.controller.Mediator;
import com.github.trganda.model.RuleModel;
import java.awt.*;

public class RuleEditorController {

  private Editor editor;
  private RuleModel rule;
  private Mediator mediator;

  public RuleEditorController(RuleModel rule) {
    Frame pFrame = FindSomething.API.userInterface().swingUtils().suiteFrame();
    this.editor = new Editor(pFrame);
    this.editor.setLocationRelativeTo(pFrame);
    this.rule = rule;

    this.setupEventListener();
  }

  public RuleEditorController(RuleModel rule, Mediator mediator) {
    this(rule);
    this.mediator = mediator;
    this.mediator.registerRuleEditorController(this);
  }

  private void setupEventListener() {
    this.editor
        .getEditorButtonsPane()
        .getCancel()
        .addActionListener(
            e -> {
              this.editor.pack();
              this.editor.setVisible(false);
            });

    this.editor
        .getEditorButtonsPane()
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
              if (this.editor.getOp() == Operation.ADD) {
                Config.getInstance().syncRules(rule.getGroup(), r, Operation.ADD);
              } else if (this.editor.getOp() == Operation.EDT) {
                // remove the old rule first
                Config.getInstance().syncRules(rule.getGroup(), rule.getRule(), Operation.DEL);
                Config.getInstance().syncRules(rule.getGroup(), r, Operation.ADD);
              }
              this.editor.setVisible(false);
            });
  }

  public void updateView(RuleModel rule) {
    this.rule = rule;
    this.editor.getNameField().setText(rule.getRule().getName());
    this.editor.getRegexField().setText(rule.getRule().getRegex());
    this.editor.getScope().setSelectedItem(rule.getRule().getScope());
    this.editor.getSensitive().setSelected(rule.getRule().isSensitive());
    this.editor.setOp(Operation.EDT);
    this.editor.pack();
    this.editor.setVisible(true);
  }
}

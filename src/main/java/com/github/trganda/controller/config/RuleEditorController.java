package com.github.trganda.controller.config;

import com.github.trganda.FindSomething;
import com.github.trganda.components.config.Editor;
import com.github.trganda.config.Config;
import com.github.trganda.config.Operation;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.config.Scope;
import com.github.trganda.model.RuleModel;
import java.awt.*;

public class RuleEditorController {

  private Editor editor;
  private RuleModel rule;

  public RuleEditorController(RuleModel rule) {
    Frame pFrame = FindSomething.API.userInterface().swingUtils().suiteFrame();
    this.editor = new Editor(pFrame);
    this.editor.setLocationRelativeTo(pFrame);
    this.rule = rule;

    this.setupEventListener();
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
              Rule r = Rule.builder()
                      .enabled(true)
                      .name(this.editor.getNameField().getText())
                      .regex(this.editor.getRegexField().getText())
                      .captureGroup(this.editor.getGroupField().getText())
                      .scope((Scope) this.editor.getScope().getSelectedItem()).sensitive(this.editor.getSensitive().isSelected()).build();
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

  public void updateEditorAndView(RuleModel rule) {
    this.rule = rule;
    this.editor.setRule(rule.getRule());
    this.editor.setOp(Operation.EDT);
    this.editor.pack();
    this.editor.setVisible(true);
  }
}

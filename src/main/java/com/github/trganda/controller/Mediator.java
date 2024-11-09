package com.github.trganda.controller;

import com.github.trganda.controller.config.RuleController;
import com.github.trganda.controller.config.RuleEditorController;
import com.github.trganda.model.RuleModel;

public class Mediator {

  private RuleEditorController editorController;
  private RuleController ruleInnerController;
  private RuleModel ruleModel;

  public Mediator(RuleModel ruleModel) {
    this.ruleModel = ruleModel;
  }

  public void updateEditor(RuleModel ruleModel) {
    this.ruleModel = ruleModel;
    this.editorController.updateRule(ruleModel);
  }

  public void registerRuleEditorController(RuleEditorController editorController) {
    this.editorController = editorController;
  }

  public void registerRuleController(RuleController ruleInnerController) {
    this.ruleInnerController = ruleInnerController;
  }
}

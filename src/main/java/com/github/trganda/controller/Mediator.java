package com.github.trganda.controller;

import com.github.trganda.controller.config.EditorController;
import com.github.trganda.controller.config.RuleInnerController;
import com.github.trganda.model.RuleModel;

public class Mediator {

  private EditorController editorController;
  private RuleInnerController ruleInnerController;
  private RuleModel ruleModel;

  public Mediator(RuleModel ruleModel) {
    this.ruleModel = ruleModel;
  }

  public void updateEditor(RuleModel ruleModel) {
    this.ruleModel = ruleModel;
    this.editorController.updateRule(ruleModel);
  }
}

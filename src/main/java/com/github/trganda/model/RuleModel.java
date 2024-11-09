package com.github.trganda.model;

import com.github.trganda.config.Rules.Rule;
import lombok.Data;

@Data
public class RuleModel {
  private String group;
  private Rule rule;
}

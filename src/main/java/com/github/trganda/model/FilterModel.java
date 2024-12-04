package com.github.trganda.model;

import static com.github.trganda.config.Config.GROUP_GENERAL;

import lombok.Data;

@Data
public class FilterModel {
  private String host;
  private String ruleType;
  private String searchTerm;
  private boolean sensitive;
  private boolean negative;

  private static final FilterModel FILTER_MODEL = new FilterModel();

  private FilterModel() {
    this.ruleType = GROUP_GENERAL;
    this.host = "*";
  }

  public static FilterModel getFilterModel() {
    return FILTER_MODEL;
  }
}

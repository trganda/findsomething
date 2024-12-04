package com.github.trganda.model;

import static com.github.trganda.config.Config.GROUP_GENERAL;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

  public boolean equals(FilterModel other) {
    return this.host.equals(other.host) &&
            this.ruleType.equals(other.ruleType) &&
            this.searchTerm.equals(other.searchTerm) &&
            this.sensitive == other.sensitive &&
            this.negative == other.negative;
  }

  public FilterModel copy() {
    FilterModel copy = new FilterModel();
    copy.host = this.host;
    copy.ruleType = this.ruleType;
    copy.searchTerm = this.searchTerm;
    copy.sensitive = this.sensitive;
    copy.negative = this.negative;
    return copy;
  }

  public List<String> getModifiedFields(FilterModel other) {
    List<String> modifiedFields = new ArrayList<>();
    if (!this.host.equals(other.host)) {
      modifiedFields.add("host");
    }
    if (!this.ruleType.equals(other.ruleType)) {
      modifiedFields.add("ruleType");
    }
    if (this.searchTerm.equals(other.searchTerm)) {
      modifiedFields.add("searchTerm");
    }
    if (this.sensitive != other.sensitive) {
      modifiedFields.add("sensitive");
    }
    if (this.negative != other.negative) {
      modifiedFields.add("negative");
    }
    return modifiedFields;
  }
}

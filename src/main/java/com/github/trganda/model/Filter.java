package com.github.trganda.model;

import static com.github.trganda.config.ConfigManager.GROUP_GENERAL;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Filter {
  private String host;
  private String group;
  private String searchTerm;
  private boolean sensitive;
  private boolean negative;

  private static final Filter FILTER =
      Filter.builder()
          .host("*")
          .group(GROUP_GENERAL)
          .searchTerm("")
          .negative(false)
          .sensitive(false)
          .build();

  public static Filter getFilter() {
    return FILTER;
  }

  public void update(Filter filter) {
    this.host = filter.getHost();
    this.group = filter.getGroup();
    this.searchTerm = filter.getSearchTerm();
    this.sensitive = filter.isSensitive();
    this.negative = filter.isNegative();
  }

  public boolean equals(Filter other) {
    return this.host.equals(other.host)
        && this.group.equals(other.group)
        && this.searchTerm.equals(other.searchTerm)
        && this.sensitive == other.sensitive
        && this.negative == other.negative;
  }

  public List<String> getModifiedFields(Filter other) {
    List<String> modifiedFields = new ArrayList<>();
    if (!this.host.equals(other.host) || !this.group.equals(other.group)) {
      modifiedFields.add("1");
    }
    if ((!this.searchTerm.equals(other.searchTerm))
        || this.sensitive != other.sensitive
        || this.negative != other.negative) {
      modifiedFields.add("2");
    }

    return modifiedFields;
  }
}

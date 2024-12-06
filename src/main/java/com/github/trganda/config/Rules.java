package com.github.trganda.config;

import java.util.List;
import lombok.*;

@Data
public class Rules {
  private List<Group> groups;

  // Getters and Setters
  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> rules) {
    this.groups = rules;
  }

  public List<Rule> getRulesWithGroup(String group) {
    if (groups.stream().filter(rule -> rule.getGroup().equals(group)).findAny().isEmpty()) {
      return List.of();
    }
    return groups.stream()
        .filter(rule -> rule.getGroup().equals(group))
        .findFirst()
        .orElse(new Group())
        .getRule();
  }

  public static class Group {
    private String group;
    private List<Rule> rule;

    // Getters and Setters
    public String getGroup() {
      return group;
    }

    public void setGroup(String group) {
      this.group = group;
    }

    public List<Rule> getRule() {
      return rule;
    }

    public void setRule(List<Rule> rule) {
      this.rule = rule;
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PUBLIC)
  public static class Rule {
    private boolean enabled;
    private String name;
    private String regex;
    private String captureGroup;
    private Scope scope;
    private boolean sensitive;

    public Object[] toObjectArray() {
      return new Object[] {enabled, name, regex, captureGroup, scope, sensitive};
    }
  }
}

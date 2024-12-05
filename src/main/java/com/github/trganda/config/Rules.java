package com.github.trganda.config;

import java.util.List;
import lombok.Data;

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
  public static class Rule {
    private boolean enabled;
    private String name;
    private String regex;
    private Scope scope;
    private boolean sensitive;

    public Rule() {}

    public Rule(boolean enabled, String name, String regex, Scope scope, boolean sensitive) {
      this.enabled = enabled;
      this.name = name;
      this.regex = regex;
      this.scope = scope;
      this.sensitive = sensitive;
    }
  }
}

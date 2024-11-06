package com.github.trganda.config;

import java.util.List;

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

  public static class Rule {
    private boolean enabled;
    private String name;
    private String regex;
    private boolean sensitive;

    public Rule() {}

    public Rule(boolean enabled, String name, String regex, boolean sensitive) {
      this.enabled = enabled;
      this.name = name;
      this.regex = regex;
      this.sensitive = sensitive;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getRegex() {
      return regex;
    }

    public void setRegex(String regex) {
      this.regex = regex;
    }

    public boolean isSensitive() {
      return sensitive;
    }

    public void setSensitive(boolean sensitive) {
      this.sensitive = sensitive;
    }
  }
}

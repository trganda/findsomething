package com.github.trganda.config;

public enum Scope {
  RESPONSE_HEADER("response header"),
  RESPONSE_BODY("response body");

  private final String description;

  // Constructor
  Scope(String description) {
    this.description = description;
  }

  // Getter method for the description
  public String getDescription() {
    return description;
  }
}

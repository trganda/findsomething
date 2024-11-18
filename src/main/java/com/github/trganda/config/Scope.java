package com.github.trganda.config;

public enum Scope {
  RESPONSE_HEADER("response header"),
  RESPONSE_BODY("response body"),
  REQUEST_HEADER("request header"),
  REQUEST_BODY("request body"),
  // Path without query parameter
  REQUEST_PATH("request path"),
  // Query params in the request
  REQUEST_QUERY("request query");

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

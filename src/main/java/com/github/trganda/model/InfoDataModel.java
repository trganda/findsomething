package com.github.trganda.model;

import lombok.Data;

@Data
public class InfoDataModel {
  private final Long id;

  private String ruleName;

  private final String results;

  private final String host;

  public InfoDataModel(Long id, String ruleName, String results, String host) {
    this.id = id;
    this.ruleName = ruleName;
    this.results = results;
    this.host = host;
  }

  public Object[] getInfoData() {
    Object[] data = new Object[] {results};
    return data;
  }
}

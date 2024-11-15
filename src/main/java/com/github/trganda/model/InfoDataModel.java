package com.github.trganda.model;

import lombok.Data;

@Data
public class InfoDataModel {
  private final Long id;

  private final String results;

  private final String host;

  public InfoDataModel(Long id, String results, String host) {
    this.id = id;
    this.results = results;
    this.host = host;
  }

  public Object[] getInfoData() {
    Object[] data = new Object[] {results};
    return data;
  }
}

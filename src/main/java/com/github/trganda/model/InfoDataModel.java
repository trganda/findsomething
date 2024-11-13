package com.github.trganda.model;

import lombok.Data;

@Data
public class InfoDataModel {
  private final Long id;

  private final String results;

  public InfoDataModel(Long id, String results) {
    this.id = id;
    this.results = results;
  }

  public Object[] getInfoData() {
    Object[] data = new Object[] {results};
    return data;
  }
}

package com.github.trganda.model;

public class InfoDataModel {
  private static Long id = 0L;

  private final String results;

  public InfoDataModel(String results) {
    this.results = results;
  }

  public Object[] getInfoData() {
    Object[] data = new Object[] {id, results};
    id = id + 1;
    return data;
  }
}

package com.github.trganda.model;

import lombok.Data;

@Data
public class InfoDataModel {
  //  private final Long id;

  private String ruleName;

  private final String result;

  private final String host;

  public InfoDataModel(String ruleName, String result, String host) {
    //    this.id = id;
    this.ruleName = ruleName;
    this.result = result;
    this.host = host;
  }

  public Object[] getInfoData() {
    Object[] data = new Object[] {result};
    return data;
  }
}

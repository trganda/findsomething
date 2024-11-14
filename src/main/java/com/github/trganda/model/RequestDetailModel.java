package com.github.trganda.model;

import lombok.Data;

@Data
public class RequestDetailModel {

  private final int messageId;
  private final String method;
  private final String path;
  private final String host;
  private final int status;
  private final String time;

  public RequestDetailModel(
      int messageId, String method, String path, String host, int status, String time) {
    this.messageId = messageId;
    this.method = method;
    this.path = path;
    this.host = host;
    this.status = status;
    this.time = time;
  }

  public Object[] getRequestData() {
    return new Object[] {messageId, method, path, host, status, time};
  }
}

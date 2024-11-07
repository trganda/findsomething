package com.github.trganda.model;

public class RequestDetailModel {

  private final int messageId;
  private final String path;
  private final String host;
  private final int status;
  private final String time;

  public RequestDetailModel(int messageId, String path, String host, int status, String time) {
    this.messageId = messageId;
    this.path = path;
    this.host = host;
    this.status = status;
    this.time = time;
  }

  public Object[] getRequestData() {
    return new Object[] {messageId, path, host, status, time};
  }
}

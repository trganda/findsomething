package com.github.trganda.model;

import lombok.Data;

@Data
public class RequestDetailModel {

  private final int messageId;
  private final String method;
  private String path;
  private String host;
  private final String url;
  private final String referer;
  private final int status;
  private final String time;

  public RequestDetailModel(
      int messageId, String method, String url, String referer, int status, String time) {
    this.messageId = messageId;
    this.method = method;
    this.url = url;
    this.referer = referer;
    this.status = status;
    this.time = time;
  }

  public Object[] getRequestData() {
    return new Object[] {messageId, method, url, referer, status, time};
  }
}

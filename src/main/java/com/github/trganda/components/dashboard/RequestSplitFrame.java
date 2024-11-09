package com.github.trganda.components.dashboard;

import javax.swing.*;

public class RequestSplitFrame extends JSplitPane {

  private final RequestPane requestPane;

  private final InformationDetailsPane informationDetailsPane;

  public RequestSplitFrame() {
    requestPane = new RequestPane();
    informationDetailsPane = new InformationDetailsPane();
    this.setOrientation(VERTICAL_SPLIT);
    this.setTopComponent(informationDetailsPane);
    this.setBottomComponent(requestPane);
  }

  // public HttpRequestEditor getRequestEditor() {
  //   return requestPane.requestEditor;
  // }

  // public HttpResponseEditor getResponseEditor() {
  //   return requestPane.responseEditor;
  // }

  // public RequestPane getRequestPane() {
  //   return requestPane;
  // }

  // public InformationDetailsPane getInformationDetailsPane() {
  //   return informationDetailsPane;
  // }

}

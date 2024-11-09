package com.github.trganda.components.dashboard;

import javax.swing.*;
import lombok.Getter;

@Getter
public class RequestSplitFrame extends JSplitPane {

  private final InformationDetailsPane informationDetailsPane;
  private final RequestPane requestPane;

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

package com.github.trganda.components.dashboard;

import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import com.github.trganda.FindSomething;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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

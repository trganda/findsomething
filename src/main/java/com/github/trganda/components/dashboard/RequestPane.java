package com.github.trganda.components.dashboard;

import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import com.github.trganda.FindSomething;
import javax.swing.JTabbedPane;

public class RequestPane extends JTabbedPane {

  private final HttpRequestEditor requestEditor;
  private final HttpResponseEditor responseEditor;

  public RequestPane() {
    requestEditor = FindSomething.API.userInterface().createHttpRequestEditor();
    responseEditor = FindSomething.API.userInterface().createHttpResponseEditor();
    this.addTab("Request", requestEditor.uiComponent());
    this.addTab("Response", responseEditor.uiComponent());
  }
}

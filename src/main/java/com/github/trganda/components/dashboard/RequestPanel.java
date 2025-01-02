package com.github.trganda.components.dashboard;

import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import com.github.trganda.FindSomething;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class RequestPanel extends JPanel {

  private final HttpRequestEditor requestEditor;
  private final HttpResponseEditor responseEditor;

  public RequestPanel() {
    requestEditor = FindSomething.API.userInterface().createHttpRequestEditor();
    responseEditor = FindSomething.API.userInterface().createHttpResponseEditor();

    this.setLayout(new BorderLayout());
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Request", requestEditor.uiComponent());
    tabbedPane.addTab("Response", responseEditor.uiComponent());
    this.add(tabbedPane, BorderLayout.CENTER);
  }
}

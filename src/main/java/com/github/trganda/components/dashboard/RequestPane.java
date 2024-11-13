package com.github.trganda.components.dashboard;

import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import com.github.trganda.FindSomething;

import java.awt.Graphics;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import lombok.Getter;

@Getter
public class RequestPane extends JTabbedPane {

  private final HttpRequestEditor requestEditor;
  private final HttpResponseEditor responseEditor;

  public RequestPane() {
    requestEditor = FindSomething.API.userInterface().createHttpRequestEditor();
    responseEditor = FindSomething.API.userInterface().createHttpResponseEditor();
    this.addTab("Request", requestEditor.uiComponent());
    this.addTab("Response", responseEditor.uiComponent());
    // this.setOrientation(HORIZONTAL_SPLIT);
    // this.setLeftComponent(requestEditor.uiComponent());
    // this.setRightComponent(responseEditor.uiComponent());
    // this.setContinuousLayout(true);
    //     this.setUI(
    //     new BasicSplitPaneUI() {
    //       @Override
    //       public BasicSplitPaneDivider createDefaultDivider() {
    //         return new BasicSplitPaneDivider(this) {
    //           @Override
    //           public void paint(Graphics g) {
    //             // hidden the default divider
    //             super.paint(g);
    //           }
    //         };
    //       }
    //     });
  }
}

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

  public HttpRequestEditor getRequestEditor() {
    return requestPane.requestEditor;
  }

  public HttpResponseEditor getResponseEditor() {
    return requestPane.responseEditor;
  }

  public RequestPane getRequestPane() {
    return requestPane;
  }

  public InformationDetailsPane getInformationDetailsPane() {
    return informationDetailsPane;
  }

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

  public class InformationDetailsPane extends JScrollPane {

    private final JTable table;

    private final DefaultTableModel tableModel;

    public InformationDetailsPane() {
      table = new JTable();
      tableModel =
          new DefaultTableModel(new Object[] {"#", "Path", "Host", "Status", "Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
              return false;
            }
          };
      table.setModel(tableModel);
      this.setViewportView(table);
      this.addComponentListener(
          new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
              super.componentResized(e);
              resizePane();
            }
          });
    }

    private void resizePane() {
      int width = table.getWidth();
      table.getColumnModel().getColumn(0).setPreferredWidth((int) (width * 0.1));
      table.getColumnModel().getColumn(1).setPreferredWidth((int) (width * 0.4));
      table.getColumnModel().getColumn(2).setPreferredWidth((int) (width * 0.3));
      table.getColumnModel().getColumn(3).setPreferredWidth((int) (width * 0.1));
      table.getColumnModel().getColumn(4).setPreferredWidth((int) (width * 0.1));
    }

    public JTable getTable() {
      return table;
    }

    public DefaultTableModel getTableModel() {
      return tableModel;
    }
  }
}

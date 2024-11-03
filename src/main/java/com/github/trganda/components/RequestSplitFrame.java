package com.github.trganda.components;

import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
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
        BasicSplitPaneUI ui = (BasicSplitPaneUI) this.getUI();
        BasicSplitPaneDivider divider = ui.getDivider();
        divider.setBorder(null);
    }

    public HttpRequestEditor getRequestEditor() {
        return requestPane.getRequestEditor();
    }

    public HttpResponseEditor getResponseEditor() {
        return requestPane.getResponseEditor();
    }

    public JTable getRequestDetailsTable() {
        return informationDetailsPane.getTable();
    }

    public DefaultTableModel getRequestDetailsTableModel() {
        return informationDetailsPane.getTableModel();
    }
}

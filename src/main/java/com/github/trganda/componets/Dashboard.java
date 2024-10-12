package com.github.trganda.componets;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Dashboard extends JPanel {

    private MontoyaApi api;
    private DefaultTableModel model;

    public Dashboard(MontoyaApi api) {
        this.api = api;
        this.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        model = new DefaultTableModel(new Object[]{"ID", "URl", "Host", "Referer", "Method", "status", "Result", "describe", "Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This will make all cells of the table non-editable
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane tablePane = new JScrollPane(table);

        splitPane.setTopComponent(tablePane);

        JTabbedPane tabs = new JTabbedPane();
        HttpRequestEditor requestEditor = api.userInterface().createHttpRequestEditor();
        HttpResponseEditor responseEditor = api.userInterface().createHttpResponseEditor();

        tabs.addTab("Request", requestEditor.uiComponent());
        tabs.addTab("Response", responseEditor.uiComponent());

        splitPane.setBottomComponent(tabs);

        this.add(splitPane);
    }

}

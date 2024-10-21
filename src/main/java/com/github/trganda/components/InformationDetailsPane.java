package com.github.trganda.components;

import burp.api.montoya.MontoyaApi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class InformationDetailsPane extends JScrollPane {

    private final JTable table;

    private final DefaultTableModel tableModel;

    private final MontoyaApi api;

    public InformationDetailsPane(MontoyaApi api) {
        this.api = api;
        table = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"#", "Path", "Host", "Status", "Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        this.setViewportView(table);
        this.addComponentListener(new ComponentAdapter() {
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

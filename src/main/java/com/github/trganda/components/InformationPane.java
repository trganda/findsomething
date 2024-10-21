package com.github.trganda.components;

import burp.api.montoya.MontoyaApi;
import com.github.trganda.handler.DataChangeListener;
import com.github.trganda.model.InfoDataModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InformationPane extends JPanel implements DataChangeListener {

    private final MontoyaApi api;
    private JTable infoTable;
    private DefaultTableModel infoTableModel;

    public InformationPane(MontoyaApi api) {
        this.api = api;
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resizePane();
            }
        });
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboBox.getPreferredSize().height));
        this.add(comboBox);
        this.add(Box.createVerticalStrut(10));
        setupTable();
    }

    private void setupTable() {
        infoTable = new JTable();

        infoTableModel = new DefaultTableModel(new Object[]{"#", "Info"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        infoTable.setModel(infoTableModel);
        JScrollPane infoTableScrollPane = new JScrollPane(infoTable);
        this.add(infoTableScrollPane);
    }

    private void resizePane() {
        int infoTableWidth = infoTable.getWidth();
        infoTable.getColumnModel().getColumn(0).setPreferredWidth((int) (infoTableWidth * 0.1));
        infoTable.getColumnModel().getColumn(1).setPreferredWidth((int) (infoTableWidth * 0.9));
    }

    @Override
    public void onDataChanged(List<InfoDataModel> data) {
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() {
                ArrayList<Object[]> rdata = new ArrayList<>();
                for (InfoDataModel row : data) {
                    rdata.add(row.getInfoData());
                }
                return rdata;
            }

            @Override
            protected void done() {
                // update when work done
                try {
                    List<Object[]> rows = get();
                    for (Object[] row : rows) {
                        infoTableModel.addRow(row);
                    }
                    infoTableModel.fireTableDataChanged();
                } catch (InterruptedException | ExecutionException e) {
                    api.logging().logToError(new RuntimeException(e));
                }
            }
        };
        worker.execute();
    }

    public JTable getInfoTable() {
        return infoTable;
    }

    public DefaultTableModel getInfoTableModel() {
        return infoTableModel;
    }
}

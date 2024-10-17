package com.github.trganda.componets.pane;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.trganda.handler.DataChangeListener;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.RequestDataModel;
import com.github.trganda.model.cache.CachePool;
import com.github.trganda.utils.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FilterSplitPane extends JSplitPane implements DataChangeListener {

    private JTable infoTable;
    private DefaultTableModel infoTableModel;
    private JTable reqInfoTable;
    private DefaultTableModel reqInfoTableModel;
    private MontoyaApi api;

    public FilterSplitPane(MontoyaApi api) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.api = api;
        infoTable = new JTable();
        reqInfoTable = new JTable();

        infoTableModel = new DefaultTableModel(new Object[]{"#", "Info"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This will make all cells of the table non-editable
                return false;
            }
        };
        infoTable.setModel(infoTableModel);
        infoTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        registerEvent();

        reqInfoTableModel = new DefaultTableModel(new Object[]{"#", "Path", "Host", "Status", "Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This will make all cells of the table non-editable
                return false;
            }
        };
        reqInfoTable.setModel(reqInfoTableModel);
        reqInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        JScrollPane infoTableScrollPane = new JScrollPane(infoTable);
        JScrollPane filterTableScrollPane = new JScrollPane(reqInfoTable);

        this.setLeftComponent(infoTableScrollPane);
        this.setRightComponent(filterTableScrollPane);
        this.setResizeWeight(0.4);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizePanel();
            }
        });
    }

    private void registerEvent() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemClear = new JMenuItem("Delete");
        menuItemClear.addActionListener(e -> {
            int row = infoTable.getSelectedRow();
            infoTableModel.removeRow(row);
        });
        popupMenu.add(menuItemClear);
        infoTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = infoTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    String info = (String) infoTable.getModel().getValueAt(row, 1);
                    String hashKey = Utils.calHash(info);
                    List<RequestDataModel> reqInfos = CachePool.getRequestDataModelList(hashKey);

                    SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
                        @Override
                        protected List<Object[]> doInBackground() {
                            List<Object[]> infos = new ArrayList<>();
                            for (RequestDataModel reqInfo : reqInfos) {
                                infos.add(reqInfo.getRequestData());
                            }
                            return infos;
                        }

                        @Override
                        protected void done() {
                            reqInfoTableModel.getDataVector().removeAllElements();
                            // update when work done
                            try {
                                List<Object[]> rows = get();
                                for (Object[] row : rows) {
                                    reqInfoTableModel.addRow(row);
                                }
                                reqInfoTableModel.fireTableDataChanged();
                            } catch (InterruptedException | ExecutionException e) {
                                api.logging().logToError(new RuntimeException(e));
                            }
                        }
                    };
                    worker.execute();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                int row = infoTable.rowAtPoint(e.getPoint());
                infoTable.setRowSelectionInterval(row, row);
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    private void resizePanel() {
        int infoTableWidth = infoTable.getWidth();
        infoTable.getColumnModel().getColumn(0).setPreferredWidth((int) (infoTableWidth * 0.1));
        infoTable.getColumnModel().getColumn(1).setPreferredWidth((int) (infoTableWidth * 0.9));
        int filterTableWidth = reqInfoTable.getWidth();
        reqInfoTable.getColumnModel().getColumn(0).setPreferredWidth((int) (filterTableWidth * 0.1));
        reqInfoTable.getColumnModel().getColumn(1).setPreferredWidth((int) (filterTableWidth * 0.4));
        reqInfoTable.getColumnModel().getColumn(2).setPreferredWidth((int) (filterTableWidth * 0.3));
        reqInfoTable.getColumnModel().getColumn(3).setPreferredWidth((int) (filterTableWidth * 0.1));
        reqInfoTable.getColumnModel().getColumn(4).setPreferredWidth((int) (filterTableWidth * 0.1));
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

    public DefaultTableModel getReqInfoTableModel() {
        return reqInfoTableModel;
    }

    public JTable getReqInfoTable() {
        return reqInfoTable;
    }
}

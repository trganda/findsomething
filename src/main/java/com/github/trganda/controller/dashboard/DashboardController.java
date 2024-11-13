package com.github.trganda.controller.dashboard;

import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.trganda.FindSomething;
import com.github.trganda.components.dashboard.Dashboard;
import com.github.trganda.handler.DataChangeListener;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.RequestDetailModel;
import com.github.trganda.model.cache.CachePool;
import com.github.trganda.utils.Utils;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class DashboardController implements DataChangeListener {
  private Dashboard dashboard;

  public DashboardController(Dashboard dashboard) {
    this.dashboard = dashboard;
    this.setupEventListener();
  }

  private void setupEventListener() {
    JComboBox<String> selector = dashboard.getInformationPane().getSelector();
    selector.addActionListener(
        e -> {
          String group = selector.getSelectedItem().toString();
          if (group == null) {
            return;
          }

          List<InfoDataModel> data = CachePool.getInstance().getInfoData(group);
          this.updateInfoView(data);
        });

    JTable infoTable = dashboard.getInformationPane().getInfoTable();
    infoTable.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(java.awt.event.MouseEvent e) {
            int row = infoTable.rowAtPoint(e.getPoint());
            if (row == -1) {
              return;
            }

            String info = infoTable.getModel().getValueAt(row, 1).toString();
            String hashKey = Utils.calHash(info);
            List<RequestDetailModel> reqInfos =
                CachePool.getInstance().getRequestDataModelList(hashKey);
            updateDetailsView(reqInfos);
          }
        });

    JTextField filterField = dashboard.getInformationPane().getFilterField();
    filterField.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent e) {
            String val = filterField.getText();
            updateFilter(val);
          }
        });

    JTable infoDetailTable =
        dashboard.getRequestSplitFrame().getInformationDetailsPane().getTable();
    DefaultTableModel infoDetailTableModel =
        dashboard.getRequestSplitFrame().getInformationDetailsPane().getTableModel();
    infoDetailTable.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(java.awt.event.MouseEvent e) {
            int row = infoDetailTable.rowAtPoint(e.getPoint());
            if (row == -1) {
              return;
            }

            String path = (String) infoDetailTableModel.getValueAt(row, 1);
            String host = (String) infoDetailTableModel.getValueAt(row, 2);
            String hash = Utils.calHash(path, host);
            InterceptedResponse resp = CachePool.getInstance().getInterceptedResponse(hash);
            if (resp != null) {
              dashboard
                  .getRequestSplitFrame()
                  .getRequestPane()
                  .getRequestEditor()
                  .setRequest(resp.request());
              dashboard
                  .getRequestSplitFrame()
                  .getRequestPane()
                  .getResponseEditor()
                  .setResponse(resp);
            }
          }
        });
  }

  private void updateInfoView(List<InfoDataModel> data) {
    DefaultTableModel infoTableModel = dashboard.getInformationPane().getInfoTableModel();
    SwingWorker<List<Object[]>, Void> worker =
        new SwingWorker<>() {

          @Override
          protected List<Object[]> doInBackground() throws Exception {
            List<Object[]> info = new ArrayList<>();
            for (InfoDataModel row : data) {
              info.add(row.getInfoData());
            }
            return info;
          }

          @Override
          protected void done() {
            // update when work done
            try {
              infoTableModel.setRowCount(0);
              List<Object[]> rows = get();
              for (Object[] row : rows) {
                infoTableModel.addRow(row);
              }
              infoTableModel.fireTableDataChanged();
              String group =
                  dashboard.getInformationPane().getSelector().getSelectedItem().toString();
              dashboard.getStatusPane().getCountLabel().setText(String.valueOf(rows.size()));
              dashboard.getStatusPane().getGroupLabel().setText(group);
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
          ;
        };

    worker.execute();
  }

  private void updateFilter(String filter) {
    RowFilter<DefaultTableModel, Object> rf = null;
    // if current expression doesn't parse, don't update.
    try {
      // filter the second cloumn
      rf = RowFilter.regexFilter(filter, 1);
    } catch (java.util.regex.PatternSyntaxException e) {
      return;
    }
    dashboard.getInformationPane().getSorter().setRowFilter(rf);
  }

  private void updateDetailsView(List<RequestDetailModel> data) {
    DefaultTableModel infoDetailTableModel =
        dashboard.getRequestSplitFrame().getInformationDetailsPane().getTableModel();
    SwingWorker<List<Object[]>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Object[]> doInBackground() {
            List<Object[]> infos = new ArrayList<>();
            for (RequestDetailModel reqInfo : data) {
              infos.add(reqInfo.getRequestData());
            }
            return infos;
          }

          @Override
          protected void done() {
            infoDetailTableModel.setRowCount(0);
            // update when work done
            try {
              List<Object[]> rows = get();
              for (Object[] row : rows) {
                infoDetailTableModel.addRow(row);
              }
              infoDetailTableModel.fireTableDataChanged();
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };
    worker.execute();
  }

  @Override
  public void onDataChanged() {
    String group = dashboard.getInformationPane().getSelector().getSelectedItem().toString();
    List<InfoDataModel> d = CachePool.getInstance().getInfoData(group);
    if (d != null) {
      this.updateInfoView(d);
    }
  }
}

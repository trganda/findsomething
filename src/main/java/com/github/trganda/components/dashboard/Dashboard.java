package com.github.trganda.components.dashboard;

import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.trganda.FindSomething;
import com.github.trganda.model.RequestDetailModel;
import com.github.trganda.model.cache.CachePool;
import com.github.trganda.utils.Utils;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Dashboard extends JSplitPane {
  private final InformationPane informationPane;
  private final RequestSplitFrame requestSplitFrame;

  public Dashboard() {
    informationPane = new InformationPane();
    requestSplitFrame = new RequestSplitFrame();

    this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    this.setLeftComponent(informationPane);
    this.setRightComponent(requestSplitFrame);
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    FindSomething.getInstance().getHandler().registerDataChangeListener(informationPane);

    // setup event
    setupTable();
  }

  public InformationPane getFilterSplitPane() {
    return informationPane;
  }

  private void setupTable() {
    informationPane
        .getInfoTable()
        .addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                JTable table = informationPane.getInfoTable();
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                  String info =
                      (String) table.getModel().getValueAt(row, 1);
                  String hashKey = Utils.calHash(info);
                  List<RequestDetailModel> reqInfos = CachePool.getRequestDataModelList(hashKey);

                  SwingWorker<List<Object[]>, Void> worker =
                      new SwingWorker<>() {
                        @Override
                        protected List<Object[]> doInBackground() {
                          List<Object[]> infos = new ArrayList<>();
                          for (RequestDetailModel reqInfo : reqInfos) {
                            infos.add(reqInfo.getRequestData());
                          }
                          return infos;
                        }

                        @Override
                        protected void done() {
                          DefaultTableModel tableModel = requestSplitFrame.getInformationDetailsPane().getTableModel();
                          tableModel.setRowCount(0);
                          // update when work done
                          try {
                            List<Object[]> rows = get();
                            for (Object[] row : rows) {
                              tableModel.addRow(row);
                            }
                            tableModel.fireTableDataChanged();
                          } catch (InterruptedException | ExecutionException e) {
                            FindSomething.API.logging().logToError(new RuntimeException(e));
                          }
                        }
                      };
                  worker.execute();
                }
              }
            });

    requestSplitFrame.getInformationDetailsPane().getTable()
        .addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                JTable table = requestSplitFrame.getInformationDetailsPane().getTable();
                DefaultTableModel tableModel = requestSplitFrame.getInformationDetailsPane().getTableModel();
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                  String path =
                      (String) tableModel.getValueAt(row, 1);
                  String host =
                      (String) tableModel.getValueAt(row, 2);
                  String hash = Utils.calHash(path, host);
                  InterceptedResponse resp = CachePool.getInterceptedResponse(hash);

                  requestSplitFrame.getRequestEditor().setRequest(resp.request());
                  requestSplitFrame.getResponseEditor().setResponse(resp);
                }
              }
            });
  }
}

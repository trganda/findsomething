package com.github.trganda.components;

import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.trganda.FindSomething;
import com.github.trganda.model.RequestDataModel;
import com.github.trganda.model.cache.CachePool;
import com.github.trganda.utils.Utils;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

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
    BasicSplitPaneUI ui = (BasicSplitPaneUI) this.getUI();
    BasicSplitPaneDivider divider = ui.getDivider();
    divider.setBorder(null);

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
                int row = informationPane.getInfoTable().rowAtPoint(e.getPoint());
                if (row >= 0) {
                  String info =
                      (String) informationPane.getInfoTable().getModel().getValueAt(row, 1);
                  String hashKey = Utils.calHash(info);
                  List<RequestDataModel> reqInfos = CachePool.getRequestDataModelList(hashKey);

                  SwingWorker<List<Object[]>, Void> worker =
                      new SwingWorker<>() {
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
                          requestSplitFrame
                              .getRequestDetailsTableModel()
                              .getDataVector()
                              .removeAllElements();
                          // update when work done
                          try {
                            List<Object[]> rows = get();
                            for (Object[] row : rows) {
                              requestSplitFrame.getRequestDetailsTableModel().addRow(row);
                            }
                            requestSplitFrame.getRequestDetailsTableModel().fireTableDataChanged();
                          } catch (InterruptedException | ExecutionException e) {
                            FindSomething.API.logging().logToError(new RuntimeException(e));
                          }
                        }
                      };
                  worker.execute();
                }
                super.mouseClicked(e);
              }
            });

    requestSplitFrame
        .getRequestDetailsTable()
        .addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                int row = requestSplitFrame.getRequestDetailsTable().rowAtPoint(e.getPoint());
                if (row >= 0) {
                  String path =
                      (String) requestSplitFrame.getRequestDetailsTable().getValueAt(row, 1);
                  String host =
                      (String) requestSplitFrame.getRequestDetailsTable().getValueAt(row, 2);
                  String hash = Utils.calHash(path, host);
                  InterceptedResponse resp = CachePool.getInterceptedResponse(hash);

                  requestSplitFrame.getRequestEditor().setRequest(resp.request());
                  requestSplitFrame.getResponseEditor().setResponse(resp);
                }
              }
            });
  }
}

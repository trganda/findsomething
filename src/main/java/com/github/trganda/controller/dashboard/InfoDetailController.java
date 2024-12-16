package com.github.trganda.controller.dashboard;

import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.trganda.FindSomething;
import com.github.trganda.components.dashboard.InformationDetailsPane;
import com.github.trganda.components.dashboard.RequestPane;
import com.github.trganda.model.RequestDetailModel;
import com.github.trganda.utils.Utils;
import com.github.trganda.utils.cache.CachePool;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InfoDetailController {

  private InformationDetailsPane informationDetailsPane;
  private RequestPane requestPane;

  public InfoDetailController(
      InformationDetailsPane informationDetailsPane, RequestPane requestPane) {
    this.informationDetailsPane = informationDetailsPane;
    this.requestPane = requestPane;
    this.setupEventListener();
  }

  private void setupEventListener() {
    // Information details
    JTable infoDetailTable = informationDetailsPane.getTable();
    infoDetailTable.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(java.awt.event.MouseEvent e) {
            int row = infoDetailTable.rowAtPoint(e.getPoint());
            if (row == -1) {
              return;
            }

            String messageId = infoDetailTable.getValueAt(row, 0).toString();
            String url = infoDetailTable.getValueAt(row, 2).toString();
            String hash = Utils.calHash(messageId, url);
            InterceptedResponse resp = CachePool.getInstance().getInterceptedResponse(hash);
            if (resp != null) {
              requestPane.getRequestEditor().setRequest(resp.request());
              requestPane.getResponseEditor().setResponse(resp);
            }
          }
        });
  }

  public void updateDetailsView(List<RequestDetailModel> data) {
    DefaultTableModel infoDetailTableModel = informationDetailsPane.getTableModel();
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
              JTable infoDetailTable = informationDetailsPane.getTable();
              // select the first row
              infoDetailTable.setRowSelectionInterval(0, 0);
              // trigger the mouse event
              infoDetailTable.dispatchEvent(
                  new MouseEvent(
                      infoDetailTable,
                      MouseEvent.MOUSE_CLICKED,
                      System.currentTimeMillis(),
                      0,
                      infoDetailTable.getCellRect(0, 0, true).x + 5,
                      infoDetailTable.getCellRect(0, 0, true).y + 5,
                      1,
                      false));
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };
    worker.execute();
  }
}

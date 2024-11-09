package com.github.trganda.components.dashboard;

import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.trganda.FindSomething;
import com.github.trganda.model.RequestDetailModel;
import com.github.trganda.model.cache.CachePool;
import com.github.trganda.utils.Utils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Dashboard extends JPanel {
  private final InformationPane informationPane;
  private final RequestSplitFrame requestSplitFrame;

  private JSplitPane dashSplitPane;

  private StatusPane statusPane;

  public Dashboard() {
    informationPane = new InformationPane();
    requestSplitFrame = new RequestSplitFrame();
    statusPane = new StatusPane();

    dashSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    dashSplitPane.setLeftComponent(informationPane);
    dashSplitPane.setRightComponent(requestSplitFrame);

    this.setLayout(new GridBagLayout());
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    this.add(dashSplitPane, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.LINE_END;
    this.add(statusPane, gbc);

    FindSomething.getInstance().getHandler().registerDataChangeListener(informationPane);

    // setup event
    // setupTable();
  }

  // private void setupTable() {
  //   informationPane
  //       .getInfoTable()
  //       .addMouseListener(
  //           new MouseAdapter() {
  //             @Override
  //             public void mouseClicked(MouseEvent e) {
  //               JTable table = informationPane.getInfoTable();
  //               int row = table.rowAtPoint(e.getPoint());
  //               if (row >= 0) {
  //                 String info = (String) table.getModel().getValueAt(row, 1);
  //                 String hashKey = Utils.calHash(info);
  //                 List<RequestDetailModel> reqInfos =
  //                     CachePool.getInstance().getRequestDataModelList(hashKey);

  //                 SwingWorker<List<Object[]>, Void> worker =
  //                     new SwingWorker<>() {
  //                       @Override
  //                       protected List<Object[]> doInBackground() {
  //                         List<Object[]> infos = new ArrayList<>();
  //                         for (RequestDetailModel reqInfo : reqInfos) {
  //                           infos.add(reqInfo.getRequestData());
  //                         }
  //                         return infos;
  //                       }

  //                       @Override
  //                       protected void done() {
  //                         DefaultTableModel tableModel =
  //                             requestSplitFrame.getInformationDetailsPane().getTableModel();
  //                         tableModel.setRowCount(0);
  //                         // update when work done
  //                         try {
  //                           List<Object[]> rows = get();
  //                           for (Object[] row : rows) {
  //                             tableModel.addRow(row);
  //                           }
  //                           tableModel.fireTableDataChanged();
  //                         } catch (InterruptedException | ExecutionException e) {
  //                           FindSomething.API.logging().logToError(new RuntimeException(e));
  //                         }
  //                       }
  //                     };
  //                 worker.execute();
  //               }
  //             }
  //           });

  //   requestSplitFrame
  //       .getInformationDetailsPane()
  //       .getTable()
  //       .addMouseListener(
  //           new MouseAdapter() {
  //             @Override
  //             public void mouseClicked(MouseEvent e) {
  //               JTable table = requestSplitFrame.getInformationDetailsPane().getTable();
  //               DefaultTableModel tableModel =
  //                   requestSplitFrame.getInformationDetailsPane().getTableModel();
  //               int row = table.rowAtPoint(e.getPoint());
  //               if (row >= 0) {
  //                 String path = (String) tableModel.getValueAt(row, 1);
  //                 String host = (String) tableModel.getValueAt(row, 2);
  //                 String hash = Utils.calHash(path, host);
  //                 InterceptedResponse resp = CachePool.getInstance().getInterceptedResponse(hash);

  //                 requestSplitFrame.getRequestEditor().setRequest(resp.request());
  //                 requestSplitFrame.getResponseEditor().setResponse(resp);
  //               }
  //             }
  //           });
  // }

  public class StatusPane extends JPanel {

    private JLabel countLabel;

    private JLabel groupLabel;

    public StatusPane() {
      countLabel = new JLabel("0");
      groupLabel = new JLabel("0");

      this.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();

      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.insets = new Insets(0, 0, 0, 5);
      gbc.anchor = GridBagConstraints.LINE_END;
      this.add(new JLabel("Count:"), gbc);

      gbc.gridx = 1;
      gbc.gridy = 0;
      this.add(countLabel, gbc);

      gbc.gridx = 2;
      gbc.gridy = 0;
      this.add(new JLabel("Group:"), gbc);

      gbc.gridx = 3;
      gbc.gridy = 0;
      gbc.insets = new Insets(0, 0, 0, 0);
      this.add(groupLabel, gbc);
    }
  }
}

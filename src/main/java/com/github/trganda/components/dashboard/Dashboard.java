package com.github.trganda.components.dashboard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;
import lombok.Getter;

@Getter
public class Dashboard extends JPanel {
  private InformationPane informationPane;
  private RequestSplitFrame requestSplitFrame;
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

    // FindSomething.getInstance().getHandler().registerDataChangeListener(informationPane);
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
  //                 InterceptedResponse resp =
  // CachePool.getInstance().getInterceptedResponse(hash);

  //                 requestSplitFrame.getRequestEditor().setRequest(resp.request());
  //                 requestSplitFrame.getResponseEditor().setResponse(resp);
  //               }
  //             }
  //           });
  // }
}

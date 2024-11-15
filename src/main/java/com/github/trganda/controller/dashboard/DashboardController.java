package com.github.trganda.controller.dashboard;

import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.trganda.FindSomething;
import com.github.trganda.components.common.PlaceHolderTextField;
import com.github.trganda.components.common.SuggestionCombox;
import com.github.trganda.components.common.SuggestionKeyListener;
import com.github.trganda.components.dashboard.Dashboard;
import com.github.trganda.components.dashboard.FilterPane;
import com.github.trganda.handler.DataChangeListener;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.RequestDetailModel;
import com.github.trganda.utils.Utils;
import com.github.trganda.utils.cache.CachePool;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class DashboardController implements DataChangeListener {
  private Dashboard dashboard;
  private FilterPane filterPane;

  public DashboardController(Dashboard dashboard) {
    this.dashboard = dashboard;
    this.filterPane = dashboard.getRequestSplitFrame().getInformationDetailsPane().getFilterPane();
    this.setupEventListener();
  }

  private void setupEventListener() {
    // Group selector
    JComboBox<String> selector = this.filterPane.getInformationFilter().getSelector();
    selector.addActionListener(
        e -> {
          if (selector.getSelectedIndex() >= 0) {
            String group = selector.getSelectedItem().toString();

            List<InfoDataModel> data = CachePool.getInstance().getInfoData(group);
            this.updateInfoView(data);
          }
        });

    // Status bar
    String g = selector.getSelectedItem().toString();
    if (g != null) {
      dashboard.getStatusPane().getGroupLabel().setText(g);
    }

    // Info table
    JTable infoTable = dashboard.getInformationPane().getInfoTable();
    infoTable.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(java.awt.event.MouseEvent e) {
            int row = infoTable.rowAtPoint(e.getPoint());
            if (row == -1) {
              return;
            }

            String info = infoTable.getModel().getValueAt(row, 0).toString();
            String hashKey = Utils.calHash(info);
            List<RequestDetailModel> reqInfos =
                CachePool.getInstance().getRequestDataModelList(hashKey);
            updateDetailsView(reqInfos);
          }
        });

    // search filter of info panel
    PlaceHolderTextField filterField = this.filterPane.getInformationFilter().getFilterField();
    JCheckBox sensitiveCheckBox = this.filterPane.getInformationFilter().getSensitive();
    filterField.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent e) {
            String val = filterField.getText();
            updateFilter(val, sensitiveCheckBox.isSelected(), filterField.isPlaceholderActive());
          }
        });

    sensitiveCheckBox.addActionListener(
        e -> {
          String val = filterField.getText();
          updateFilter(val, sensitiveCheckBox.isSelected(), filterField.isPlaceholderActive());
        });

    // search filter of host
    SuggestionCombox<String> hostComboBox = this.filterPane.getHostFilter().getHostComboBox();
    DefaultComboBoxModel<String> hostComboBoxModel =
        this.filterPane.getHostFilter().getHostComboBoxModel();
    JTextField hostComboBoxEditor = (JTextField) hostComboBox.getEditor().getEditorComponent();
    hostComboBoxEditor.addKeyListener(new SuggestionKeyListener(hostComboBox, hostComboBoxModel));
    hostComboBox.addActionListener(
        e -> {
          if (hostComboBox.getSelectedIndex() > -1 && selector.getSelectedIndex() > -1) {
            String selectedHost = hostComboBox.getSelectedItem().toString();
            String group = selector.getSelectedItem().toString();

            List<InfoDataModel> data =
                CachePool.getInstance().getInfoData(group).stream()
                    .filter(d -> Utils.isDomainMatch(selectedHost, d.getHost()))
                    .collect(Collectors.toList());
            this.updateInfoView(data);
          } else {
            // Default view with filter
            if (selector.getSelectedIndex() >= 0) {
              String group = selector.getSelectedItem().toString();

              List<InfoDataModel> data = CachePool.getInstance().getInfoData(group);
              this.updateInfoView(data);
            }
          }
        });

    // info details
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

            String path = (String) infoDetailTableModel.getValueAt(row, 2);
            String host = (String) infoDetailTableModel.getValueAt(row, 3);
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
              updateStatus();
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
          ;
        };

    worker.execute();
  }

  private void updateFilter(String filter, boolean sensitive, boolean isPlaceholderActive) {
    RowFilter<DefaultTableModel, Object> rf = null;
    if (!isPlaceholderActive) {
      // if current expression doesn't parse, don't update.
      try {
        // filter the first cloumn
        if (!sensitive) {
          filter = "(?i)" + filter;
        }
        rf = RowFilter.regexFilter(filter, 0);
      } catch (java.util.regex.PatternSyntaxException e) {
        return;
      }
    }
    dashboard.getInformationPane().getSorter().setRowFilter(rf);
    updateStatus();
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

  private void updateStatus() {
    String group = filterPane.getInformationFilter().getSelector().getSelectedItem().toString();

    int size = dashboard.getInformationPane().getInfoTable().getRowCount();
    dashboard.getStatusPane().getCountLabel().setText(String.valueOf(size));
    dashboard.getStatusPane().getGroupLabel().setText(group);
  }

  @Override
  public void onDataChanged() {
    String group =
        this.filterPane.getInformationFilter().getSelector().getSelectedItem().toString();
    List<InfoDataModel> d = CachePool.getInstance().getInfoData(group);
    if (d != null && d.size() > 0) {
      this.updateInfoView(d);
    }
  }
}

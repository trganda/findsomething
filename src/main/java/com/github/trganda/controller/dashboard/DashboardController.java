package com.github.trganda.controller.dashboard;

import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import com.github.trganda.FindSomething;
import com.github.trganda.components.common.*;
import com.github.trganda.components.dashboard.Dashboard;
import com.github.trganda.components.dashboard.FilterPane;
import com.github.trganda.components.dashboard.InformationPane;
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
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DashboardController implements DataChangeListener {
  private Dashboard dashboard;
  private FilterPane filterPane;
  private InformationPane infoPane;
  private JComboBox<String> groupSelector;
  private HttpRequestEditor requestEditor;
  private HttpResponseEditor responseEditor;

  public DashboardController(Dashboard dashboard) {
    this.dashboard = dashboard;
    this.filterPane = dashboard.getRequestSplitFrame().getInformationDetailsPane().getFilterPane();
    this.infoPane = dashboard.getInformationPane();
    this.groupSelector = this.filterPane.getHostFilter().getSelector();
    this.requestEditor = dashboard.getRequestSplitFrame().getRequestPane().getRequestEditor();
    this.responseEditor = dashboard.getRequestSplitFrame().getRequestPane().getResponseEditor();
    this.setupEventListener();
  }

  private void setupEventListener() {
    // Group selector
    this.groupSelector.addActionListener(
        e -> {
          if (groupSelector.getSelectedIndex() >= 0) {
            String group = groupSelector.getSelectedItem().toString();
            // TODO: hide empty tab
            List<InfoDataModel> data = CachePool.getInstance().getInfoData(group);
            this.updateInfoView(data);
          }
        });

    // Status bar
    String g = this.groupSelector.getSelectedItem().toString();
    if (g != null) {
      dashboard.getStatusPane().getGroupLabel().setText(g);
    }

    // Information tab
    infoPane.addChangeListener(
        new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            String group = groupSelector.getSelectedItem().toString();
            List<InfoDataModel> data = CachePool.getInstance().getInfoData(group);
            updateActiveInfoView(data);
          }
        });

    // Setup click event listener for 'All' tab in information panel
    this.setupTabEventListener(infoPane.getActiveTabView());

    // Search filter of information panel
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
          if (dashboard.getInformationPane().getSelectedIndex() >= 0) {
            FindSomething.API
                .logging()
                .logToOutput(dashboard.getInformationPane().getSelectedComponent().toString());
          }
        });

    // Search filter of host
    SuggestionComboBox suggestionComboBox = this.filterPane.getHostFilter().getSuggestion();
    JComboBox<String> hostComboBox = suggestionComboBox.getHostComboBox();
    JTextField hostTextField = suggestionComboBox.getHostTextField();

    hostTextField
        .getDocument()
        .addDocumentListener(new SuggestionDocumentListener(suggestionComboBox));
    hostTextField.addKeyListener(new SuggestionKeyListener(suggestionComboBox));
    hostComboBox.addActionListener(
        e -> {
          if (!suggestionComboBox.isMatching() && hostComboBox.getSelectedIndex() >= 0) {
            String selectedHost = hostComboBox.getSelectedItem().toString();
            hostTextField.setText(selectedHost);
            hostComboBox.setPopupVisible(false);
          }
        });

    // Information details
    JTable infoDetailTable =
        dashboard.getRequestSplitFrame().getInformationDetailsPane().getTable();
    infoDetailTable.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(java.awt.event.MouseEvent e) {
            int row = infoDetailTable.rowAtPoint(e.getPoint());
            if (row == -1) {
              return;
            }

            String path = infoDetailTable.getValueAt(row, 2).toString();
            String host = infoDetailTable.getValueAt(row, 3).toString();
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

  private void setupTabEventListener(JScrollPane wraps) {
    JTable table = (JTable) wraps.getViewport().getView();

    // Information tab
    ListSelectionModel selectionModel = table.getSelectionModel();
    selectionModel.addListSelectionListener(
        new ListSelectionListener() {

          @Override
          public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
              int selectedRow = table.getSelectedRow();
              if (selectedRow == -1) {
                return;
              }
              String info = table.getValueAt(selectedRow, 0).toString();
              String hashKey = Utils.calHash(info);
              List<RequestDetailModel> reqInfos =
                  CachePool.getInstance().getRequestDataModelList(hashKey);
              updateDetailsView(reqInfos);

              // update filter of request/response filter
              // TODO: set as rule scope, and active the search
              requestEditor.setSearchExpression(info);
              responseEditor.setSearchExpression(info);
            }
          }
        });
  }

  private void updateInfoView(List<InfoDataModel> data) {
    // Update active tab view
    this.updateActiveInfoView(data);

    // Classified with rule name
    Map<String, List<InfoDataModel>> classified =
        data.stream().collect(Collectors.groupingBy(d -> d.getRuleName()));

    // Update other tab view with rule name
    classified.forEach(
        (ruleName, vals) -> {
          if (vals.size() > 0) {
            JScrollPane wraps = null;
            int idx = infoPane.getTabComponentIndexByName(ruleName);
            if (idx == -1) {
              // Add tab with the rule name
              wraps = (JScrollPane) infoPane.addTableTab(ruleName);
              // Setup event listener for the new tab
              this.setupTabEventListener(wraps);
            }
          }
        });
  }

  private void updateActiveInfoView(List<InfoDataModel> data) {
    int selectedIndex = infoPane.getSelectedIndex();
    if (selectedIndex != -1) {
      String title = infoPane.getTitleAt(selectedIndex);
      JScrollPane wrap = (JScrollPane) infoPane.getComponentAt(selectedIndex);
      JTable table = (JTable) wrap.getViewport().getView();
      DefaultTableModel model = (DefaultTableModel) table.getModel();

      if (title == InformationPane.ALL) {
        this.updateInfoView(model, data);
      } else {
        List<InfoDataModel> filteredData =
            data.stream().filter(d -> d.getRuleName().equals(title)).collect(Collectors.toList());
        updateInfoView(model, filteredData);
      }
    }
  }

  private void updateInfoView(DefaultTableModel model, List<InfoDataModel> data) {
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
              model.setRowCount(0);
              List<Object[]> rows = get();
              for (Object[] row : rows) {
                model.addRow(row);
              }
              model.fireTableDataChanged();
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
    RowFilter<TableModel, Object> rf = null;
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

    // update all tabs
    for (int i = 0; i < this.infoPane.getTabCount(); i++) {
      JScrollPane wrap = (JScrollPane) this.infoPane.getComponentAt(i);
      JTable table = (JTable) wrap.getViewport().getView();
      TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
      sorter.setRowFilter(rf);
      table.setRowSorter(sorter);
    }
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
    String group = groupSelector.getSelectedItem().toString();

    JScrollPane wrap = infoPane.getActiveTabView();
    JTable table = (JTable) wrap.getViewport().getView();

    int size = table.getRowCount();
    dashboard.getStatusPane().getCountLabel().setText(String.valueOf(size));
    dashboard.getStatusPane().getGroupLabel().setText(group);
  }

  @Override
  public void onDataChanged() {
    String group = this.groupSelector.getSelectedItem().toString();
    List<InfoDataModel> data = CachePool.getInstance().getInfoData(group);
    if (data.size() == 0) {
      return;
    }

    updateInfoView(data);
  }
}

package com.github.trganda.controller.dashboard;

import com.github.trganda.FindSomething;
import com.github.trganda.components.dashboard.InformationPane;
import com.github.trganda.handler.DataChangeListener;
import com.github.trganda.handler.FilterChangeListener;
import com.github.trganda.model.Filter;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.RequestDetailModel;
import com.github.trganda.utils.Utils;
import com.github.trganda.utils.cache.CachePool;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class InfoController implements DataChangeListener, FilterChangeListener {

  private final InformationPane infoPane;
  private final InfoDetailController infoDetailController;

  public InfoController(InformationPane infoPane, InfoDetailController infoDetailController) {
    this.infoPane = infoPane;
    this.infoDetailController = infoDetailController;
    this.setupEventListener();
  }

  private void setupEventListener() {
    // Information tab
    infoPane
        .getTabbedPane()
        .addChangeListener(
            e -> {
              updateActiveInfoView(Filter.getFilter());
            });

    // Setup click event listener for 'All' tab
    this.setupTabEventListener(infoPane.getActiveTabView());
  }

  private void updateActiveInfoView(List<InfoDataModel> data) {
    int selectedIndex = infoPane.getTabbedPane().getSelectedIndex();
    if (selectedIndex != -1) {
      String title = infoPane.getTabbedPane().getTitleAt(selectedIndex);
      JScrollPane wrap = (JScrollPane) infoPane.getTabbedPane().getComponentAt(selectedIndex);
      JTable table = (JTable) wrap.getViewport().getView();
      DefaultTableModel model = (DefaultTableModel) table.getModel();

      if (title.equals(InformationPane.ALL)) {
        this.updateInfoView(model, data);
      } else {
        SwingWorker<List<InfoDataModel>, Void> worker =
            new SwingWorker<>() {
              @Override
              protected List<InfoDataModel> doInBackground() {
                return data.stream()
                    .filter(d -> d.getRuleName().equals(title))
                    .collect(Collectors.toList());
              }

              @Override
              protected void done() {
                try {
                  updateInfoView(model, get());
                } catch (InterruptedException | ExecutionException e) {
                  FindSomething.API.logging().logToError(new RuntimeException(e));
                }
              }
            };
        worker.execute();
      }
    }
  }

  private void updateTabView(List<InfoDataModel> data) {
    // Classified with rule name
    Map<String, List<InfoDataModel>> classified =
        data.stream().collect(Collectors.groupingBy(InfoDataModel::getRuleName));

    // Create other tab view with rule name
    classified.forEach(
        (ruleName, vals) -> {
          if (!vals.isEmpty()) {
            JTable table = null;
            int idx = infoPane.getTabComponentIndexByName(ruleName);
            if (idx == -1) {
              // Add tab with the rule name
              table = infoPane.addTableTab(ruleName);
              // Setup event listener for the new tab
              this.setupTabEventListener(table);
            }
          }
        });
  }

  private void updateInfoView(DefaultTableModel model, List<InfoDataModel> data) {
    SwingWorker<List<Object[]>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Object[]> doInBackground() {
            return data.stream().map(InfoDataModel::getInfoData).collect(Collectors.toList());
          }

          @Override
          protected void done() {
            // update when work done
            try {
              model.setRowCount(0);
              List<Object[]> rows = get();
              rows.forEach(model::addRow);
              model.fireTableDataChanged();
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };

    worker.execute();
  }

  private void setupTabEventListener(JTable table) {
    // Information tab
    ListSelectionModel selectionModel = table.getSelectionModel();
    selectionModel.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting()) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
              return;
            }
            String info = table.getValueAt(selectedRow, 0).toString();
            String hashKey = Utils.calHash(info);
            List<RequestDetailModel> reqInfos =
                CachePool.getInstance().getRequestDataModelList(hashKey);

            infoDetailController.updateDetailsView(reqInfos);
          }
        });
  }

  @Override
  public void onDataChanged() {
    updateActiveInfoView(Filter.getFilter());
  }

  @Override
  public void onFilterChanged() {}

  public void updateActiveInfoView(Filter filter) {
    SwingWorker<List<InfoDataModel>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<InfoDataModel> doInBackground() {
            String ruleType = filter.getGroup();
            String selectedHost = filter.getHost();
            return CachePool.getInstance().getInfoData(ruleType).stream()
                .filter(d -> Utils.isDomainMatch(selectedHost, d.getHost()))
                .collect(Collectors.toList());
          }

          @Override
          protected void done() {
            try {
              List<InfoDataModel> data = get();
              // Update active tab view
              updateActiveInfoView(data);
              // Create other tab view with rule name if not exist
              updateTabView(data);
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };

    worker.execute();
  }

  public void updateTableFilter(
      String filter, boolean sensitive, boolean negative, boolean isPlaceholderActive) {
    RowFilter<TableModel, Object> rf = null;
    if (!isPlaceholderActive) {
      // if current expression doesn't parse, don't update.
      try {
        if (!sensitive) {
          filter = "(?i)" + filter;
        }

        if (negative) {
          // filter the first column
          rf = RowFilter.notFilter(RowFilter.regexFilter(filter, 0));
        } else {
          rf = RowFilter.regexFilter(filter, 0);
        }

      } catch (java.util.regex.PatternSyntaxException e) {
        return;
      }
    }

    // update all tabs
    for (int i = 0; i < this.infoPane.getTabbedPane().getTabCount(); i++) {
      JScrollPane wrap = (JScrollPane) this.infoPane.getTabbedPane().getComponentAt(i);
      JTable table = (JTable) wrap.getViewport().getView();
      TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
      sorter.setRowFilter(rf);
      table.setRowSorter(sorter);
    }
  }
}

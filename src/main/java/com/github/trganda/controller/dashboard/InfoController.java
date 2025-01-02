package com.github.trganda.controller.dashboard;

import com.github.trganda.components.dashboard.InformationPanel;
import com.github.trganda.components.dashboard.StatusPanel;
import com.github.trganda.handler.DataChangeListener;
import com.github.trganda.handler.FilterChangeListener;
import com.github.trganda.model.Filter;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.RequestDetailModel;
import com.github.trganda.utils.Utils;
import com.github.trganda.utils.cache.CachePool;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class InfoController implements DataChangeListener, FilterChangeListener {

  private final InformationPanel infoPane;
  private final InfoDetailController infoDetailController;
  private StatusPanel statusPanel;

  public InfoController(
      InformationPanel infoPane,
      StatusPanel statusPanel,
      InfoDetailController infoDetailController) {
    this.infoPane = infoPane;
    this.statusPanel = statusPanel;
    this.infoDetailController = infoDetailController;
    this.setupEventListener();
  }

  /**
   * Set up event listeners for the information panel.
   *
   * <p>
   * When the tab of the information panel is changed, the information view in the
   * active tab is updated with the current filter.
   *
   * <p>
   * The click event listener is set up for the 'All' tab. When a row is clicked, the
   * request details view is updated with the corresponding request data.
   */
  private void setupEventListener() {
    // Information tab
    infoPane
        .getTabbedPane()
        .addChangeListener(
            e -> {
              Filter filter = Filter.getFilter();
              updateInfoView(filter, true);
              updateTableFilter(filter.getSearchTerm(), filter.isSensitive(), filter.isNegative());
            });

    // Setup click event listener for 'All' tab
    this.setupTabEventListener(infoPane.getActiveTabView());
  }

  public void updateInfoView(Filter filter, boolean onlyActivate) {
    String ruleType = filter.getGroup();
    String selectedHost = filter.getHost();
    int selectedIndex = infoPane.getTabbedPane().getSelectedIndex();

    String title = infoPane.getTabbedPane().getTitleAt(selectedIndex);
    JScrollPane wrap = (JScrollPane) infoPane.getTabbedPane().getComponentAt(selectedIndex);
    JTable table = (JTable) wrap.getViewport().getView();
    DefaultTableModel model = (DefaultTableModel) table.getModel();

    SwingWorker<List<InfoDataModel>, Void> worker =
        new SwingWorker<>() {
          private List<InfoDataModel> data = new ArrayList<>();

          @Override
          protected List<InfoDataModel> doInBackground() throws Exception {
            if (selectedIndex != -1) {
              data =
                  CachePool.getInstance().getInfoData(ruleType).stream()
                      .filter(d -> Utils.isDomainMatch(selectedHost, d.getHost()))
                      .collect(Collectors.toList());

              if (!title.equals(InformationPanel.ALL)) {
                return data.stream()
                    .filter(d -> d.getRuleName().equals(title))
                    .collect(Collectors.toList());
              }
            }

            return data;
          }

          @Override
          protected void done() {
            try {
              List<InfoDataModel> info = get();
              if (!onlyActivate) {
                // Create other tab view with rule name if not exist
                updateTabView(info);
              }
              model.setRowCount(0);
              info.forEach(i -> model.addRow(i.getInfoData()));
              model.fireTableDataChanged();
              statusPanel.getCountLabel().setText(infoPane.getActiveTabView().getRowCount() + "");
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }
        };
    worker.execute();
  }

  /**
   * Creates other tab view with rule name.
   *
   * <p>
   * The given data is classified with rule name and for each rule name, a new tab is
   * created if the tab does not exist. The event listener is set up for the new tab.
   *
   * @param data The data to classify and create tab for.
   */
  private void updateTabView(List<InfoDataModel> data) {
    // Classified with rule name
    Map<String, List<InfoDataModel>> classified =
        data.stream().collect(Collectors.groupingBy(InfoDataModel::getRuleName));

    // Clear other tab first
//    infoPane.clearTab();

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

  /**
   * Sets up the given table to react to row selection changes.
   * <p>
   * When a row is selected in the table, this method is called. It uses the selected row to
   * determine which request detail views to update.
   *
   * @param table the table to set up the row selection listener for
   */
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

  private void updateInfoView(List<InfoDataModel> data) {

    DefaultTableModel model = (DefaultTableModel) infoPane.getActiveTabView().getModel();
    SwingWorker<Object[], Void> worker =
        new SwingWorker<>() {
          @Override
          protected Object[] doInBackground() throws Exception {
//          data = data.filter();

            return data.stream().map(InfoDataModel::getInfoData).toArray(Object[]::new);
          }

          @Override
          protected void done() {
            try {
              model.addRow(get());
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }
        };

    worker.execute();
  }

  /**
   * Triggered when data source changed, update the active info view to reflect the changes.
   */
  @Override
  public void onDataChanged(List<InfoDataModel> data) {
    if (data == null || data.isEmpty()) {
      return;
    }
    updateInfoView(Filter.getFilter(), false);
  }

  @Override
  public void onFilterChanged() {}

  /**
   * Updates the table filter.
   * <p>
   * The filter string is interpreted as a regular expression. If the filter string is empty,
   * the filter is disabled. If the filter string is not empty but does not parse as a regular
   * expression, the filter is not changed. If the filter string does parse as a regular
   * expression, the filter is updated. If the negative flag is set, the filter is inverted.
   * <p>
   * The filter is applied to all tabs in the {@code infoPane} component.
   *
   * @param filter the filter string
   * @param sensitive whether the filter is case-sensitive
   * @param negative whether the filter is inverted
   */
  public void updateTableFilter(String filter, boolean sensitive, boolean negative) {
    RowFilter<TableModel, Object> rf = null;
    //    if (!isPlaceholderActive) {
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

    JTable table = this.infoPane.getActiveTabView();
    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
    sorter.setRowFilter(rf);
    table.setRowSorter(sorter);
  }
}

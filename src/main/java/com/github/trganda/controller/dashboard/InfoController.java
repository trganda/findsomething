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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
            .addMouseListener(
                new MouseAdapter() {
                  @Override
                  public void mouseClicked(MouseEvent e) {
                    // TODO: ignore click event if we click on the activate tab
                    // ref: https://stackoverflow.com/questions/41528601/java-swing-how-to-detect-doubleclick-on-tab-header-in-jtabbedpane/41528659
                    updateInfoView(Filter.getFilter(), true);
                  }
            });

    // Setup click event listener for 'All' tab
    this.setupTabEventListener(infoPane.getActiveTabView());
  }

  /**
   * Updates the active tab view with the given data.
   *
   * <p>If the active tab is the 'All' tab, the data is filtered and updated directly.
   * Otherwise, a SwingWorker is used to filter the data with the rule name and update
   * the view when done.
   *
   * @param data The data to update the view with.
   */
  private void updateActivateInfoView(List<InfoDataModel> data) {
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
    infoPane.clearTab();

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
   * Updates the given table model.
   * <p>
   * This works by creating a SwingWorker that asynchronously converts the data into a list of
   * {@code Object[]} arrays, and then sets the model rows to the produced list. If the worker
   * encounters an exception, a runtime exception is logged.
   *
   * @param model the table model to update
   * @param data the data to update the model with
   */
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

  /**
   * Triggered when data source changed, update the active info view to reflect the changes.
   */
  @Override
  public void onDataChanged() {
    updateInfoView(Filter.getFilter(), false);
  }

  @Override
  public void onFilterChanged() {}

  /**
   * Update the active info view with the given filter.
   * <p>
   * This method will also update other tab views with rule name if not exist.
   */
  public void updateInfoView(Filter filter, boolean onlyActivate) {
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
              if (!onlyActivate) {
                // Create other tab view with rule name if not exist
                updateTabView(data);
              }
              // Update active tab view
              updateActivateInfoView(data);
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };

    worker.execute();
  }

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
   * @param isPlaceholderActive whether the filter is currently inactive
   */
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

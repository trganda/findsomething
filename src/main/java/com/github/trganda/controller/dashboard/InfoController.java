package com.github.trganda.controller.dashboard;

import static com.github.trganda.config.ConfigManager.GROUP_GENERAL;

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
import java.util.*;
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
        .addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                JTable table = infoPane.getActiveTabView();
                statusPanel.getCountLabel().setText(String.valueOf(table.getModel().getRowCount()));
              }
            });

    // Setup click event listener for 'All' tab
    this.setupTabEventListener(infoPane.getActiveTabView());
  }

  public void refreshInfoView(Filter filter) {
    SwingWorker<List<InfoDataModel>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<InfoDataModel> doInBackground() {
            List<InfoDataModel> data =
                CachePool.getInstance().getInfoData().stream()
                    .filter(
                        d -> {
                          if (filter.getGroup().equals(GROUP_GENERAL)) {
                            return true;
                          } else {
                            return d.getGroupName().equals(filter.getGroup());
                          }
                        })
                    .filter(d -> Utils.isDomainMatch(filter.getHost(), d.getHost()))
                    .collect(Collectors.toList());

            return data;
          }

          @Override
          protected void done() {
            try {
              // Clear tabs
              infoPane.clearTab();
              List<InfoDataModel> data = get();
              DefaultTableModel model =
                  (DefaultTableModel)
                      infoPane
                          .getTabAtIndex(infoPane.getTabComponentIndexByName(InformationPanel.ALL))
                          .getModel();
              model.setRowCount(0);
              // Refresh info of 'All' tab
              updateInfoView(data, model);
              // Refresh other tab
              updateTabView(data);
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }
        };
    worker.execute();
  }

  /**
   * Creates other tab view with rule name if not exists, then update the data in the tab.
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
            } else {
              table = infoPane.getTabAtIndex(idx);
            }
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            updateInfoView(vals, model);
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

  /**
   * Updates the given table with the given data.
   *
   * <p>
   * This method creates a SwingWorker to do the work of converting the data to a format
   * suitable for the table model. It then adds each row of the data to the table model.
   *
   * @param data the data to display in the table
   * @param model the table model to update
   */
  private void updateInfoView(List<InfoDataModel> data, DefaultTableModel model) {
    SwingWorker<List<Object[]>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Object[]> doInBackground() throws Exception {
            return data.stream().map(InfoDataModel::getInfoData).collect(Collectors.toList());
          }

          @Override
          protected void done() {
            try {
              List<Object[]> data = get();
              data.forEach(model::addRow);
              model.fireTableDataChanged();
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }
        };

    worker.execute();
  }

  private void updateInfoView(List<InfoDataModel> data) {
    SwingWorker<Void, Void> worker =
        new SwingWorker<>() {
          private List<InfoDataModel> finalData = new ArrayList<>();

          @Override
          protected Void doInBackground() {
            Filter filter = Filter.getFilter();
            finalData =
                data.stream()
                    .filter(
                        d -> {
                          if (filter.getGroup().equals(GROUP_GENERAL)) {
                            return true;
                          } else {
                            return d.getGroupName().equals(filter.getGroup());
                          }
                        })
                    .filter(d -> Utils.isDomainMatch(filter.getHost(), d.getHost()))
                    .collect(Collectors.toList());
            return null;
          }

          @Override
          protected void done() {
            DefaultTableModel model = (DefaultTableModel) infoPane.getActiveTabView().getModel();
            updateInfoView(finalData, model);
            updateTabView(finalData);
            statusPanel.getCountLabel().setText(String.valueOf(model.getRowCount()));
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
    updateInfoView(data);
  }

  @Override
  public void onFilterChanged() {}
}

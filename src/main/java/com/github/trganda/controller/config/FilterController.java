package com.github.trganda.controller.config;

import static com.github.trganda.config.Config.BLACKLIST_HOST;
import static com.github.trganda.config.Config.BLACKLIST_STATUS;
import static com.github.trganda.config.Config.BLACKLIST_SUFFIX;

import com.github.trganda.FindSomething;
import com.github.trganda.components.config.FilterListInnerPane;
import com.github.trganda.config.Config;
import com.github.trganda.config.ConfigChangeListener;
import com.github.trganda.config.Operation;
import com.github.trganda.utils.Utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;

public class FilterController implements ConfigChangeListener {
  private FilterListInnerPane innerPane;

  public FilterController() {
    Config.getInstance().registerConfigListener(this);
  }

  public FilterController(FilterListInnerPane innerPane) {
    this();
    this.innerPane = innerPane;
    this.setupEventListener();
    this.loadDefaultData();
  }

  private void setupEventListener() {
    innerPane
        .getAddBlackListButton()
        .addActionListener(
            e -> {
              String val = innerPane.getInputTextField().getText();
              if (val.isEmpty() || val.equals(innerPane.getPlaceHolder())) {
                return;
              }
              // ignore if already exists same value
              if (innerPane.getBlackListTableModel().getDataVector().stream()
                      .filter(row -> row.get(0).equals(val))
                      .count()
                  > 0) {
                return;
              }
              // sync to configuration
              syncToConfig(val, Operation.ADD);
              innerPane.getInputTextField().setText("");
            });

    Color defaultColor = innerPane.getInputTextField().getForeground();
    innerPane
        .getInputTextField()
        .addKeyListener(
            new KeyAdapter() {
              @Override
              public void keyReleased(KeyEvent e) {
                String val = innerPane.getInputTextField().getText();
                // highlight if already exists
                if (innerPane.getBlackListTableModel().getDataVector().stream()
                        .filter(row -> row.get(0).equals(val))
                        .count()
                    > 0) {
                  innerPane.getInputTextField().setForeground(Color.RED);
                  innerPane
                      .getInputTextField()
                      .setFont(
                          new Font(
                              Utils.getBurpDisplayFont().getName(),
                              Font.ITALIC,
                              Utils.getBurpDisplayFont().getSize()));
                } else {
                  innerPane.getInputTextField().setForeground(defaultColor);
                  innerPane.getInputTextField().setFont(Utils.getBurpDisplayFont());
                  if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    syncToConfig(val, Operation.ADD);
                    innerPane.getInputTextField().setText("");
                  }
                }
              }
            });

    JComboBox<String> type = innerPane.getBlackListButtonsPane().getType();
    type.addActionListener(
        e -> {
          this.onConfigChange(Config.getInstance());
        });

    JButton remove = innerPane.getBlackListButtonsPane().getRemove();
    remove.addActionListener(
        e -> {
          int[] idxes = innerPane.getBlackListTable().getSelectedRows();
          for (int idx : idxes) {
            syncToConfig(
                innerPane.getBlackListTableModel().getValueAt(idx, 0).toString(), Operation.DEL);
          }
        });

    JButton clear = innerPane.getBlackListButtonsPane().getClear();
    clear.addActionListener(e -> syncToConfig("", Operation.CLR));
  }

  private void syncToConfig(String val, Operation type) {
    String selectedItem =
        innerPane.getBlackListButtonsPane().getType().getSelectedItem().toString();
    if (selectedItem != null) {
      switch (selectedItem) {
        case BLACKLIST_SUFFIX:
          Config.getInstance().syncSuffixes(val, type);
          break;
        case BLACKLIST_HOST:
          Config.getInstance().syncHosts(val, type);
          break;
        case BLACKLIST_STATUS:
          Config.getInstance().syncStatus(val, type);
          break;
      }
    }
  }

  private void loadDefaultData() {
    this.onConfigChange(Config.getInstance());
  }

  @Override
  public void onConfigChange(Config config) {
    SwingWorker<List<String[]>, Void> worker =
        new SwingWorker<List<String[]>, Void>() {
          @Override
          protected List<String[]> doInBackground() {
            List<String[]> list = new ArrayList<>();
            // sync from configuration
            String selectedItem =
                innerPane.getBlackListButtonsPane().getType().getSelectedItem().toString();
            switch (selectedItem) {
              case BLACKLIST_SUFFIX:
                config.getSuffixes().forEach(s -> list.add(new String[] {s}));
                break;
              case BLACKLIST_HOST:
                config.getHosts().forEach(s -> list.add(new String[] {s}));
                break;
              case BLACKLIST_STATUS:
                config.getStatus().forEach(s -> list.add(new String[] {s}));
                break;
            }

            return list;
          }

          @Override
          protected void done() {
            try {
              List<String[]> result = get();
              innerPane.getBlackListTableModel().setRowCount(0);
              for (String[] row : result) {
                innerPane.getBlackListTableModel().addRow(row);
              }
              innerPane.getBlackListTableModel().fireTableDataChanged();
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };
    worker.execute();
  }
}

package com.github.trganda.components.config;

import static com.github.trganda.config.Config.BLACKLIST_HOST;
import static com.github.trganda.config.Config.BLACKLIST_STATUS;
import static com.github.trganda.config.Config.BLACKLIST_SUFFIX;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class BlackListInnerButtonsPane extends JPanel {
  private final JComboBox<String> type;
  private final JButton remove;
  private final JButton clear;

  public BlackListInnerButtonsPane() {
    type = new JComboBox<>(new String[] {BLACKLIST_SUFFIX, BLACKLIST_HOST, BLACKLIST_STATUS});
    remove = new JButton("Remove");
    clear = new JButton("Clear");

    setAlign(remove, clear);
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(type);
    this.add(Box.createVerticalStrut(5));
    this.add(remove);
    this.add(Box.createVerticalStrut(5));
    this.add(clear);
    // setupButtonEventHandler();
  }

  // private void setupButtonEventHandler() {
  //   type.addActionListener(
  //       e -> {
  //         String selectedItem = (String) type.getSelectedItem();
  //         FindSomething.API.logging().logToOutput("selected item: " + selectedItem);
  //         if (selectedItem == null) {
  //           return;
  //         }
  //         switch (selectedItem) {
  //           case BLACKLIST_SUFFIX:
  //             loadBlackListWithType(Config.getInstance().getSuffixes());
  //             break;
  //           case BLACKLIST_HOST:
  //             loadBlackListWithType(Config.getInstance().getHosts());
  //             break;
  //           case BLACKLIST_STATUS:
  //             loadBlackListWithType(Config.getInstance().getStatus());
  //             break;
  //         }
  //       });

  //   remove.addActionListener(
  //       e -> {
  //         int[] idxes = blackListTable.getSelectedRows();
  //         for (int idx : idxes) {
  //           syncToConfig(blackListTableModel.getValueAt(idx, 0).toString(), Operatation.DEL);
  //         }
  //       });

  //   clear.addActionListener(e -> syncToConfig("", Operatation.CLR));
  // }

  // public void loadBlackListWithType(List<String> data) {
  //   SwingWorker<List<String[]>, Void> worker =
  //       new SwingWorker<>() {
  //         @Override
  //         protected List<String[]> doInBackground() {
  //           ArrayList<String[]> list = new ArrayList<>();
  //           for (String s : data) {
  //             list.add(new String[] {s});
  //           }
  //           return list;
  //         }

  //         @Override
  //         protected void done() {
  //           // update when work done
  //           try {
  //             blackListTableModel.setRowCount(0);
  //             List<String[]> rows = get();
  //             for (String[] row : rows) {
  //               blackListTableModel.addRow(row);
  //             }
  //           } catch (InterruptedException | ExecutionException e) {
  //             FindSomething.API.logging().logToError(new RuntimeException(e));
  //           }
  //         }
  //       };
  //   worker.execute();
  // }

  private void setAlign(JButton... buttons) {
    for (var button : buttons) {
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
    }
  }
}

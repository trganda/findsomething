package com.github.trganda.components.config;

import com.github.trganda.utils.Utils;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import lombok.Getter;

@Getter
public class BlackListInnerPane extends JPanel {

  private final String placeHolder = "Enter an new item";
  private BlackListInnerButtonsPane blackListButtonsPane;
  private JTable blackListTable;
  private DefaultTableModel blackListTableModel;
  private JButton addBlackListButton;
  private JTextField inputTextField;
  private JComponent wrap;

  public BlackListInnerPane() {
    // initialize each component
    this.setupComponents();

    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 5, 5);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(blackListButtonsPane, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 0, 0);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    this.add(wrap, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0.0;
    gbc.insets = new Insets(5, 0, 0, 5);
    gbc.anchor = GridBagConstraints.SOUTHWEST;
    this.add(addBlackListButton, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(5, 0, 0, 0);
    gbc.anchor = GridBagConstraints.WEST;
    this.add(inputTextField, gbc);
  }

  private void setupComponents() {
    addBlackListButton = new JButton("Add");
    blackListTableModel = new DefaultTableModel(new Object[] {""}, 0);
    blackListTable = new JTable(blackListTableModel);
    blackListButtonsPane = new BlackListInnerButtonsPane();

    wrap = this.setupTable();

    this.setupInputTextField();
    // addBlackListButton.addActionListener(
    //     e -> {
    //       String val = inputTextField.getText();
    //       if (val.isEmpty() || val.equals(placeHolder)) {
    //         return;
    //       }
    //       // ignore if already exist same value
    //       if (blackListTableModel.getDataVector().stream()
    //               .filter(row -> row.get(0).equals(val))
    //               .count()
    //           > 0) {
    //         return;
    //       }
    //       // sync to configuration
    //       syncToConfig(val, Operatation.ADD);
    //       inputTextField.setText("");
    //     });
  }

  private JComponent setupTable() {
    JScrollPane scrollPane = new JScrollPane(blackListTable);
    scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 160));
    scrollPane.setMaximumSize(new Dimension(scrollPane.getMaximumSize().width, 160));
    scrollPane.setMinimumSize(new Dimension(scrollPane.getMinimumSize().width, 160));

    blackListTable.setTableHeader(null);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setLeftComponent(scrollPane);
    // add a empty component
    splitPane.setRightComponent(new JPanel());

    // show suffixes blacklist default
    // blackListButtonsPane.loadBlackListWithType(Config.getInstance().getSuffixes());
    return splitPane;
  }

  private void setupInputTextField() {
    inputTextField = new JTextField(placeHolder);
    inputTextField.setFont(
        new Font(
            Utils.getBurpEditorFont().getName(), Font.ITALIC, Utils.getBurpEditorFont().getSize()));
    inputTextField.setForeground(Color.GRAY);
    inputTextField.setPreferredSize(
        new Dimension(200, addBlackListButton.getPreferredSize().height));

    inputTextField.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent e) {
            super.focusGained(e);
            if (inputTextField.getText().equals(placeHolder)) {
              inputTextField.setFont(
                  new Font(
                      Utils.getBurpEditorFont().getName(),
                      Font.PLAIN,
                      Utils.getBurpEditorFont().getSize()));
              inputTextField.setForeground(Color.BLACK);
              inputTextField.setText("");
            }
          }

          @Override
          public void focusLost(FocusEvent e) {
            super.focusLost(e);
            if (inputTextField.getText().isEmpty()) {
              inputTextField.setFont(
                  new Font(
                      Utils.getBurpEditorFont().getName(),
                      Font.ITALIC,
                      Utils.getBurpEditorFont().getSize()));
              inputTextField.setForeground(Color.GRAY);
              inputTextField.setText(placeHolder);
            }
          }
        });

    // Color defaultColor = inputTextField.getForeground();
    // inputTextField.addKeyListener(
    //     new KeyAdapter() {
    //       @Override
    //       public void keyReleased(KeyEvent e) {
    //         String val = inputTextField.getText();
    //         // highlight if already exists
    //         if (blackListTableModel.getDataVector().stream()
    //                 .filter(row -> row.get(0).equals(val))
    //                 .count()
    //             > 0) {
    //           inputTextField.setForeground(Color.RED);
    //           inputTextField.setFont(
    //               new Font(
    //                   Utils.getBurpEditorFont().getName(),
    //                   Font.ITALIC,
    //                   Utils.getBurpEditorFont().getSize()));
    //         } else {
    //           inputTextField.setForeground(defaultColor);
    //           inputTextField.setFont(Utils.getBurpEditorFont());
    //           if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    //             syncToConfig(val, Operatation.ADD);
    //             inputTextField.setText("");
    //           }
    //         }
    //       }
    //     });
  }

  // private void syncToConfig(String val, Operatation type) {
  //   String selectedItem = (String) blackListButtonsPane.type.getSelectedItem();
  //   if (selectedItem != null) {
  //     switch (selectedItem) {
  //       case BLACKLIST_SUFFIX:
  //         Config.getInstance().syncSuffixes(val, type);
  //         break;
  //       case BLACKLIST_HOST:
  //         Config.getInstance().syncHosts(val, type);
  //         break;
  //       case BLACKLIST_STATUS:
  //         Config.getInstance().syncStatus(val, type);
  //         break;
  //     }
  //   }
  // }

  // @Override
  // public void onConfigChange(Config config) {
  //   SwingWorker<List<String[]>, Void> worker =
  //       new SwingWorker<List<String[]>, Void>() {
  //         @Override
  //         protected List<String[]> doInBackground() {
  //           List<String[]> list = new ArrayList<>();
  //           // sync to configuration
  //           String selectedItem = (String) blackListButtonsPane.type.getSelectedItem();
  //           switch (selectedItem) {
  //             case BLACKLIST_SUFFIX:
  //               config.getSuffixes().forEach(s -> list.add(new String[] {s}));
  //               break;
  //             case BLACKLIST_HOST:
  //               config.getHosts().forEach(s -> list.add(new String[] {s}));
  //               break;
  //             case BLACKLIST_STATUS:
  //               config.getStatus().forEach(s -> list.add(new String[] {s}));
  //               break;
  //           }

  //           return list;
  //         }

  //         @Override
  //         protected void done() {
  //           try {
  //             List<String[]> result = get();
  //             blackListTableModel.setRowCount(0);
  //             for (String[] row : result) {
  //               blackListTableModel.addRow(row);
  //             }
  //             blackListTableModel.fireTableDataChanged();
  //           } catch (InterruptedException | ExecutionException e) {
  //             FindSomething.API.logging().logToError(new RuntimeException(e));
  //           }
  //         }
  //       };
  //   worker.execute();
  // }
}

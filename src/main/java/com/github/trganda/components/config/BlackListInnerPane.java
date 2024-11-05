package com.github.trganda.components.config;

import static com.github.trganda.config.Config.*;

import com.github.trganda.FindSomething;
import com.github.trganda.config.Config;
import com.github.trganda.config.ConfigChangeListener;
import com.github.trganda.utils.Utils;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BlackListInnerPane extends JPanel implements ConfigChangeListener {

  private final String placeHolder = "Enter an new item";
  private BlackListButtonsPane blackListButtonsPane;
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
    blackListButtonsPane = new BlackListButtonsPane();

    wrap = this.setupTable();

    this.setupInputTextField();
    addBlackListButton.addActionListener(
        e -> {
          String val = inputTextField.getText();
          if (val.isEmpty() || val.equals(placeHolder)) {
            return;
          }
          // ignore if already exist same value
          if (blackListTableModel.getDataVector().stream()
                  .filter(row -> row.get(0).equals(val))
                  .count()
              > 0) {
            return;
          }
          // sync to configuration
          String selectedItem = (String) blackListButtonsPane.type.getSelectedItem();
          if (selectedItem != null) {
            switch (selectedItem) {
              case BLACKLIST_SUFFIX:
                Config.addSuffix(val);
                break;
              case BLACKLIST_HOST:
                Config.addHost(val);
                break;
              case BLACKLIST_STATUS:
                Config.addStatus(val);
                break;
            }
          }
        });
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
    blackListButtonsPane.loadBlackListWithType(Config.getInstance().getSuffixes());
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

    Color defaultColor = inputTextField.getForeground();
    inputTextField.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent e) {
            String val = inputTextField.getText();
            // highlight if already exists
            if (blackListTableModel.getDataVector().stream()
                    .filter(row -> row.get(0).equals(val))
                    .count()
                > 0) {
              inputTextField.setForeground(Color.RED);
              inputTextField.setFont(
                  new Font(
                      Utils.getBurpEditorFont().getName(),
                      Font.ITALIC,
                      Utils.getBurpEditorFont().getSize()));
            } else {
              inputTextField.setForeground(defaultColor);
              inputTextField.setFont(Utils.getBurpEditorFont());
            }
          }
        });
  }

  private class BlackListButtonsPane extends JPanel {
    private final JComboBox<String> type;
    private final JButton remove;
    private final JButton clear;

    public BlackListButtonsPane() {
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
      setupButtonEventHandler();
    }

    private void setupButtonEventHandler() {
      type.addActionListener(
          e -> {
            String selectedItem = (String) type.getSelectedItem();
            FindSomething.API.logging().logToOutput("selected item: " + selectedItem);
            if (selectedItem == null) {
              return;
            }
            switch (selectedItem) {
              case BLACKLIST_SUFFIX:
                loadBlackListWithType(Config.getInstance().getSuffixes());
                break;
              case BLACKLIST_HOST:
                loadBlackListWithType(Config.getInstance().getHosts());
                break;
              case BLACKLIST_STATUS:
                loadBlackListWithType(Config.getInstance().getStatus());
                break;
            }
          });

      remove.addActionListener(
          e -> {
            int[] idxes = blackListTable.getSelectedRows();
            for (int idx : idxes) {
              blackListTableModel.removeRow(idx);
            }
          });

      clear.addActionListener(e -> blackListTableModel.setRowCount(0));
    }

    public void loadBlackListWithType(List<String> data) {
      SwingWorker<List<String[]>, Void> worker =
          new SwingWorker<>() {
            @Override
            protected List<String[]> doInBackground() {
              ArrayList<String[]> list = new ArrayList<>();
              for (String s : data) {
                list.add(new String[] {s});
              }
              return list;
            }

            @Override
            protected void done() {
              // update when work done
              try {
                blackListTableModel.setRowCount(0);
                List<String[]> rows = get();
                for (String[] row : rows) {
                  blackListTableModel.addRow(row);
                }
              } catch (InterruptedException | ExecutionException e) {
                FindSomething.API.logging().logToError(new RuntimeException(e));
              }
            }
          };
      worker.execute();
    }

    private void setAlign(JButton... buttons) {
      for (var button : buttons) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
      }
    }
  }

  @Override
  public void onConfigChange(Config config) {
    SwingWorker<List<Object[]>, Void> worker =
        new SwingWorker<List<Object[]>, Void>() {
          @Override
          protected List<Object[]> doInBackground() {
            List<Object[]> list = new ArrayList<>();
            // sync to configuration
            String selectedItem = (String) blackListButtonsPane.type.getSelectedItem();
            switch (selectedItem) {
              case BLACKLIST_SUFFIX:
                list.add(config.getSuffixes().toArray());
                break;
              case BLACKLIST_HOST:
                list.add(config.getHosts().toArray());
                break;
              case BLACKLIST_STATUS:
                list.add(config.getStatus().toArray());
                break;
            }

            return list;
          }

          @Override
          protected void done() {
            try {
              List<Object[]> result = get();
              blackListTableModel.setRowCount(0);
              for (Object[] row : result) {
                blackListTableModel.addRow(row);
              }
            } catch (InterruptedException | ExecutionException e) {
              FindSomething.API.logging().logToError(new RuntimeException(e));
            }
          }
        };
    worker.execute();
  }
}

package com.github.trganda.components.config;

import com.github.trganda.utils.Utils;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import lombok.Getter;

@Getter
public class FilterListInnerPane extends JPanel {

  private final String placeHolder = "Enter an new item";
  private FilterListInnerButtonsPane blackListButtonsPane;
  private JTable blackListTable;
  private DefaultTableModel blackListTableModel;
  private JButton addBlackListButton;
  private JTextField inputTextField;
  private JComponent wrap;

  public FilterListInnerPane() {
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
    blackListButtonsPane = new FilterListInnerButtonsPane();

    wrap = this.setupTable();

    this.setupInputTextField();
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

    return splitPane;
  }

  private void setupInputTextField() {
    inputTextField = new JTextField(placeHolder);
    inputTextField.setFont(
        new Font(
            Utils.getBurpDisplayFont().getName(),
            Font.PLAIN,
            Utils.getBurpDisplayFont().getSize()));
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
                      Utils.getBurpDisplayFont().getName(),
                      Font.PLAIN,
                      Utils.getBurpDisplayFont().getSize()));
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
                      Utils.getBurpDisplayFont().getName(),
                      Font.ITALIC,
                      Utils.getBurpDisplayFont().getSize()));
              inputTextField.setForeground(Color.GRAY);
              inputTextField.setText(placeHolder);
            }
          }
        });
  }
}

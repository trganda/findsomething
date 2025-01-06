package com.github.trganda.components.config;

import com.github.trganda.components.common.PlaceHolderTextField;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import lombok.Getter;

@Getter
public class GroupPanel extends JPanel {

  private JTable ruleGroupTable;
  private DefaultTableModel ruleGroupTableModel;
  private JTextField addField;
  private GroupButtonsPanel groupButtonsPanel;
  private JButton add;
  private JScrollPane scrollPane;
  private JSplitPane splitPane;

  public GroupPanel() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupComponents() {
    add = new JButton("Add");
    groupButtonsPanel = new GroupButtonsPanel();
    addField = new PlaceHolderTextField("Enter a group name");
    addField.setPreferredSize(new Dimension(200, addField.getPreferredSize().height));

    ruleGroupTableModel = new DefaultTableModel(new Object[] {""}, 0);
    ruleGroupTable = new JTable(ruleGroupTableModel);
    ruleGroupTable.setTableHeader(null);
    // Set fixed height to 160
    scrollPane = new JScrollPane(ruleGroupTable);
    scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 100));
    scrollPane.setMaximumSize(new Dimension(scrollPane.getMaximumSize().width, 100));
    scrollPane.setMinimumSize(new Dimension(scrollPane.getMinimumSize().width, 100));

    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setLeftComponent(scrollPane);
    splitPane.setRightComponent(new JPanel());
  }

  private void setupLayout() {

    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.insets = new Insets(0, 0, 15, 0);
    this.add(new JLabel("Group configuration"), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(0, 0, 10, 0);
    this.add(new JLabel("Modify the group name or add a new group"), gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(0, 0, 5, 5);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(groupButtonsPanel, gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.insets = new Insets(0, 0, 0, 0);
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.LINE_START;
    this.add(splitPane, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = 0;
    gbc.insets = new Insets(5, 0, 0, 5);
    gbc.anchor = GridBagConstraints.LINE_START;
    this.add(add, gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(5, 0, 0, 0);
    gbc.anchor = GridBagConstraints.LINE_START;
    this.add(addField, gbc);
  }
}

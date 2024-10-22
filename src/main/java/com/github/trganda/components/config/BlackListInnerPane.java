package com.github.trganda.components.config;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BlackListInnerPane extends JPanel {

    private BlackListButtonsPane2 blackListButtonsPane2;

    private JTable blackListTable;

    private DefaultTableModel blackListTableModel;

    private JButton addBlackListButton;

    private JTextField inputTextField;

    public BlackListInnerPane() {
        blackListButtonsPane2 = new BlackListButtonsPane2();
        blackListTableModel = new DefaultTableModel();
        blackListTable = new JTable(blackListTableModel);
        addBlackListButton = new JButton("Add");

        JScrollPane scrollPane = new JScrollPane(blackListTable);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 160));
        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(new JPanel());

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(layout);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        this.add(blackListButtonsPane2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(splitPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 0, 0, 5);
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        this.add(addBlackListButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 0, 0, 0);
        gbc.anchor = GridBagConstraints.WEST;
        inputTextField = new JTextField("Enter an new item");
        inputTextField.setFont(new Font("Arial", Font.ITALIC, 12));
        inputTextField.setForeground(Color.GRAY);
        inputTextField.setPreferredSize(new Dimension(200, addBlackListButton.getPreferredSize().height));
        this.add(inputTextField, gbc);
    }

}

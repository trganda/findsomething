package com.github.trganda.components.config;

import com.github.trganda.FindSomething;
import com.github.trganda.config.Config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BlackListInnerPane extends JPanel {

    private BlackListButtonsPane blackListButtonsPane;

    private JTable blackListTable;

    private DefaultTableModel blackListTableModel;

    private JButton addBlackListButton;

    private JTextField inputTextField;

    public BlackListInnerPane() {
        blackListButtonsPane = new BlackListButtonsPane();
        addBlackListButton = new JButton("Add");
        blackListTableModel = new DefaultTableModel(new Object[]{"Value"}, 0);
        blackListTable = new JTable(blackListTableModel);


        JScrollPane scrollPane = new JScrollPane(blackListTable);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 160));
        scrollPane.setColumnHeaderView(null);
        blackListTable.setTableHeader(null);
        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(new JPanel());
        setupTable();

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(layout);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        this.add(blackListButtonsPane, gbc);

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
        setupInputTextField();
        this.add(inputTextField, gbc);
    }

    private void setupTable() {
        SwingWorker<java.util.List<String[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected java.util.List<String[]> doInBackground() {
                ArrayList<String[]> rdata = new ArrayList<>();
                for (String suffix : Config.suffixes.split("\\|")) {
                    rdata.add(new String[]{suffix});

                }
                return rdata;
            }

            @Override
            protected void done() {
                // update when work done
                try {
                    List<String[]> rows = get();
                    for (String[] row : rows) {
                        blackListTableModel.addRow(row);
                    }
                    blackListTableModel.fireTableDataChanged();
                } catch (InterruptedException | ExecutionException e) {
                    FindSomething.api.logging().logToError(new RuntimeException(e));
                }
            }
        };
        worker.execute();
    }

    private void setupInputTextField() {
        String placeHolder = "Enter an new item";
        inputTextField = new JTextField(placeHolder);
        inputTextField.setFont(new Font("Arial", Font.ITALIC, 12));
        inputTextField.setForeground(Color.GRAY);
        inputTextField.setPreferredSize(new Dimension(200, addBlackListButton.getPreferredSize().height));

        inputTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (inputTextField.getText().equals(placeHolder)) {
                    inputTextField.setFont(new Font("Arial", Font.PLAIN, 12));
                    inputTextField.setForeground(Color.BLACK);
                    inputTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (inputTextField.getText().isEmpty()) {
                    inputTextField.setFont(new Font("Arial", Font.ITALIC, 12));
                    inputTextField.setForeground(Color.GRAY);
                    inputTextField.setText(placeHolder);
                }
            }
        });
    }

}

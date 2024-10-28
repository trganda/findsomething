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

import static com.github.trganda.config.Config.*;

public class BlackListInnerPane extends JPanel {

    private final String placeHolder = "Enter an new item";
    private BlackListButtonsPane blackListButtonsPane;
    private JTable blackListTable;
    private DefaultTableModel blackListTableModel;
    private JButton addBlackListButton;
    private JTextField inputTextField;

    public BlackListInnerPane() {
        addBlackListButton = new JButton("Add");
        blackListTableModel = new DefaultTableModel(new Object[]{""}, 0);
        blackListTable = new JTable(blackListTableModel);
        blackListButtonsPane = new BlackListButtonsPane();

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
        this.add(setupTable(), gbc);

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
        setupInputTextField();
        this.add(inputTextField, gbc);
        setupComponents();
    }

    private void setupComponents() {
        addBlackListButton.addActionListener(e -> {
            String val = inputTextField.getText();
            if (val.isEmpty() || val.equals(placeHolder)) {
                return;
            }
            blackListTableModel.addRow(new Object[]{val});
        });
    }

    private JSplitPane setupTable() {
        JScrollPane scrollPane = new JScrollPane(blackListTable);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 160));
        scrollPane.setMaximumSize(new Dimension(scrollPane.getMaximumSize().width, 160));
        scrollPane.setMinimumSize(new Dimension(scrollPane.getMinimumSize().width, 160));

        blackListTable.setTableHeader(null);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(new JPanel());

        // show suffixes blacklist default
        blackListButtonsPane.loadBlackListWithType(Config.getSuffixes());
        return splitPane;
    }

    private void setupInputTextField() {
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

    private class BlackListButtonsPane extends JPanel {
        private JComboBox<String> type;
        private JButton remove;
        private JButton clear;

        public BlackListButtonsPane() {
            type = new JComboBox<>(new String[]{BLACKLIST_SUFFIX, BLACKLIST_HOST, BLACKLIST_STATUS});
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
            type.addActionListener(e -> {
                String selectedItem = (String) type.getSelectedItem();
                if (selectedItem == null) {
                    return;
                }
                switch (selectedItem) {
                    case BLACKLIST_SUFFIX:
                        loadBlackListWithType(Config.getSuffixes());
                        break;
                    case BLACKLIST_HOST:
                        loadBlackListWithType(Config.getHosts());
                        break;
                }
            });

            remove.addActionListener(e -> {
                int[] idxes = blackListTable.getSelectedRows();
                for (int idx : idxes) {
                    blackListTableModel.removeRow(idx);
                }
            });

            clear.addActionListener(e -> {
                blackListTableModel.setRowCount(0);
            });
        }

        public void loadBlackListWithType(String[] data) {
            FindSomething.api.logging().logToOutput("data " + data.length);
            SwingWorker<java.util.List<String[]>, Void> worker = new SwingWorker<>() {
                @Override
                protected java.util.List<String[]> doInBackground() {
                    ArrayList<String[]> list = new ArrayList<>();
                    for (String s : data) {
                        list.add(new String[]{s});

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
                        FindSomething.api.logging().logToError(new RuntimeException(e));
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

}

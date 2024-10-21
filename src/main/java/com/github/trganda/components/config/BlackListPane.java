package com.github.trganda.components.config;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BlackListPane extends JPanel {

    private BlackListButtonsPane blackListButtonsPane;

    private JTextField blackListTextField;

    private JTable table;

    private DefaultTableModel tableModel;

    public BlackListPane() {
        blackListButtonsPane = new BlackListButtonsPane();
        blackListTextField = new JTextField("Input");
        tableModel = new DefaultTableModel(new Object[]{"Value"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        this.setBorder(new TitledBorder("Black List"));

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(layout);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        this.add(blackListTextField, constraints);

        constraints.weightx = 0;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.insets = new Insets(5, 0, 5, 5);
        this.add(blackListButtonsPane, constraints);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 5, 5, 5);
        this.add(new JScrollPane(table), constraints);
    }
}

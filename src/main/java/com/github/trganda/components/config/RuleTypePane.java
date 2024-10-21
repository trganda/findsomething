package com.github.trganda.components.config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RuleTypePane extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public RuleTypePane() {

        this.tableModel = new DefaultTableModel(new Object[]{"Regex"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        this.table = new JTable(tableModel);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(layout);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(new JButton("Add Rule"), constraints);


        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.add(new JScrollPane(table), constraints);
    }

}

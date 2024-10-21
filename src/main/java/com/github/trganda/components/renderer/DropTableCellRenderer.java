package com.github.trganda.components.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class DropTableCellRenderer implements TableCellRenderer {

    private final JComboBox<String> comboBox;

    public DropTableCellRenderer() {
        comboBox = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
        comboBox.addActionListener(e -> {
            comboBox.getSelectedItem();
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (column == 1) {
            return comboBox;
        } else {
            return new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

}

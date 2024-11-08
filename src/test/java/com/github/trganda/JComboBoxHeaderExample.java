package com.github.trganda;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class JComboBoxHeaderExample {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          JFrame frame = new JFrame("JTable Header with JComboBox");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

          // Sample data for the JTable
          Object[][] data = {
            {"Item 1", "Value 1"},
            {"Item 2", "Value 2"},
            {"Item 3", "Value 3"},
            {"Item 4", "Value 4"},
          };
          String[] columns = {"Item", "Value"};

          // Create the JTable
          JTable table = new JTable(data, columns);
          TableColumnModel columnModel = table.getColumnModel();

          // Create the JComboBox and add it to the header
          JComboBox<String> comboBox =
              new JComboBox<>(new String[] {"Item", "Option 1", "Option 2", "Option 3"});

          // Custom TableCellRenderer for the JComboBox header
          TableColumn comboColumn = columnModel.getColumn(0);
          comboColumn.setHeaderRenderer(
              new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                  return comboBox;
                }
              });

          // Add a mouse listener to the table header to show the combo box dropdown on click
          JTableHeader header = table.getTableHeader();
          header.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                  // Check if the first column header (where the combo box is) was clicked
                  int columnIndex = table.columnAtPoint(e.getPoint());
                  if (columnIndex == 0) { // Assuming the combo box is in the first column header
                    comboBox.showPopup(); // Show the dropdown menu of the combo box
                    comboBox.setVisible(true);
                  }
                }
              });

          // Optional: handle JComboBox selection
          comboBox.addActionListener(
              e -> {
                String selected = (String) comboBox.getSelectedItem();
                System.out.println("Selected header option: " + selected);
                // You can add functionality here based on the selected item.
              });

          // Add table to a scroll pane
          JScrollPane scrollPane = new JScrollPane(table);
          frame.add(scrollPane, BorderLayout.CENTER);

          // Display the frame
          frame.setSize(400, 200);
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
        });
  }
}

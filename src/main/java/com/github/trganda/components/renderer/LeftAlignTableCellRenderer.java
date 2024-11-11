package com.github.trganda.components.renderer;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class LeftAlignTableCellRenderer implements TableCellRenderer {
  private TableCellRenderer headerRenderer;

  public LeftAlignTableCellRenderer(TableCellRenderer headerRenderer) {
    this.headerRenderer = headerRenderer;
  }

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    JLabel lbl =
        (JLabel)
            headerRenderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
    // apply default renderer to boolean column
    if (table.getColumnClass(column) == Boolean.class) {
      return lbl;
    }
    // apply left-aligned renderer to each column header
    lbl.setHorizontalAlignment(SwingConstants.LEFT);
    return lbl;
  }
}

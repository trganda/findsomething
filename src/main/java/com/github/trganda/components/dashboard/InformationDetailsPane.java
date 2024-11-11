package com.github.trganda.components.dashboard;

import com.github.trganda.components.renderer.LeftAlignTableCellRenderer;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import lombok.Getter;

@Getter
public class InformationDetailsPane extends JScrollPane {

  private JTable table;
  private DefaultTableModel tableModel;

  public InformationDetailsPane() {
    table = new JTable();
    tableModel =
        new DefaultTableModel(new Object[] {"#", "Path", "Host", "Status", "Time"}, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
    table.setModel(tableModel);
    TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
    table.getTableHeader().setDefaultRenderer(new LeftAlignTableCellRenderer(headerRenderer));
    this.setViewportView(table);
    this.addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            resizePane();
          }
        });
  }

  private void resizePane() {
    int width = table.getWidth();
    table.getColumnModel().getColumn(0).setPreferredWidth((int) (width * 0.05));
    table.getColumnModel().getColumn(1).setPreferredWidth((int) (width * 0.4));
    table.getColumnModel().getColumn(2).setPreferredWidth((int) (width * 0.3));
    table.getColumnModel().getColumn(3).setPreferredWidth((int) (width * 0.1));
    table.getColumnModel().getColumn(4).setPreferredWidth((int) (width * 0.15));
  }
}

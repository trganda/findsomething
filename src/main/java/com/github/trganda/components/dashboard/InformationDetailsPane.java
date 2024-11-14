package com.github.trganda.components.dashboard;

import com.github.trganda.components.renderer.LeftAlignTableCellRenderer;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import lombok.Getter;

@Getter
public class InformationDetailsPane extends JPanel {

  private JTable table;
  private DefaultTableModel tableModel;
  private JScrollPane scrollPane;
  private FilterPane filterPane;

  public InformationDetailsPane() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupComponents() {
    table = new JTable();
    tableModel =
        new DefaultTableModel(new Object[] {"#", "Method", "Path", "Host", "Status", "Time"}, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
    table.setModel(tableModel);
    TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
    table.getTableHeader().setDefaultRenderer(new LeftAlignTableCellRenderer(headerRenderer));

    scrollPane = new JScrollPane(table);
    scrollPane.addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            resizePane();
          }
        });
    scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 200));

    filterPane = new FilterPane();
  }

  private void setupLayout() {
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 5, 0);
    gbc.anchor = GridBagConstraints.CENTER;
    this.add(filterPane, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(scrollPane, gbc);
  }

  private void resizePane() {
    int width = table.getWidth();
    table.getColumnModel().getColumn(0).setPreferredWidth((int) (width * 0.05));
    table.getColumnModel().getColumn(1).setPreferredWidth((int) (width * 0.08));
    table.getColumnModel().getColumn(2).setPreferredWidth((int) (width * 0.37));
    table.getColumnModel().getColumn(3).setPreferredWidth((int) (width * 0.3));
    table.getColumnModel().getColumn(4).setPreferredWidth((int) (width * 0.1));
    table.getColumnModel().getColumn(5).setPreferredWidth((int) (width * 0.1));
  }
}

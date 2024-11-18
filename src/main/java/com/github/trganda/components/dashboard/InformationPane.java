package com.github.trganda.components.dashboard;

import com.github.trganda.components.renderer.LeftAlignTableCellRenderer;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import lombok.Getter;

@Getter
public class InformationPane extends JTabbedPane {
  public static final String ALL = "All";
  private final String filterPlaceHolder = "Search";

  public InformationPane() {
    this.setMinimumSize(new Dimension(420, this.getPreferredSize().height));
    this.setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
    this.setupComponents();
  }

  private void setupComponents() {
    // Add tab with 'All' default
    JComponent wrap = createTableView();
    this.addTab(ALL, wrap);
  }

  private JComponent createTableView() {
    JTable infoTable = new JTable();
    DefaultTableModel infoTableModel =
        new DefaultTableModel(new Object[] {"Info"}, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
    infoTable.setModel(infoTableModel);
    TableCellRenderer headerRenderer = infoTable.getTableHeader().getDefaultRenderer();
    infoTable.getTableHeader().setDefaultRenderer(new LeftAlignTableCellRenderer(headerRenderer));

    JScrollPane infoTableScrollPane = new JScrollPane(infoTable);
    return infoTableScrollPane;
  }

  public JComponent addTableTab(String tabName) {
    JComponent wrap = createTableView();
    this.addTab(tabName, wrap);
    return wrap;
  }

  public int getTabComponentIndexByName(String tabName) {
    for (int i = 0; i < this.getTabCount(); i++) {
      if (this.getTitleAt(i).equals(tabName)) {
        return i;
      }
    }
    return -1; // Tab not found
  }

  public JScrollPane getActiveTabView() throws RuntimeException {
    if (this.getSelectedIndex() >= 0) {
      return (JScrollPane) this.getSelectedComponent();
    }
    throw new RuntimeException("No active tab found.");
  }

  public void clearTab() {
    int index = getTabComponentIndexByName(ALL);
    if (index != -1) {
      Component all = this.getComponentAt(index);
      this.removeAll();
      this.addTab(ALL, all);
    }
  }
}

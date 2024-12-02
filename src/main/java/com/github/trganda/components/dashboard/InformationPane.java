package com.github.trganda.components.dashboard;

import static javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT;

import com.github.trganda.components.renderer.LeftAlignTableCellRenderer;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import lombok.Getter;

@Getter
public class InformationPane extends JPanel {
  public static final String ALL = "All";
  private final String filterPlaceHolder = "Search";
  private JTabbedPane tabbedPane;

  public InformationPane() {
    this.setMinimumSize(new Dimension(420, this.getPreferredSize().height));
    this.setupComponents();
  }

  private void setupComponents() {
    // Add tab with 'All' default
    JComponent wrap = createTableView();
    this.setLayout(new BorderLayout());
    tabbedPane = new JTabbedPane();
    tabbedPane.setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
    tabbedPane.addTab(ALL, wrap);

    this.add(tabbedPane, BorderLayout.CENTER);
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
    tabbedPane.addTab(tabName, wrap);
    return wrap;
  }

  public int getTabComponentIndexByName(String tabName) {
    for (int i = 0; i < tabbedPane.getTabCount(); i++) {
      if (tabbedPane.getTitleAt(i).equals(tabName)) {
        return i;
      }
    }
    return -1; // Tab not found
  }

  public JScrollPane getActiveTabView() throws RuntimeException {
    if (tabbedPane.getSelectedIndex() >= 0) {
      return (JScrollPane) tabbedPane.getSelectedComponent();
    }
    throw new RuntimeException("No active tab found.");
  }

  public void clearTab() {
    int index = getTabComponentIndexByName(ALL);
    if (index != -1) {
      Component all = tabbedPane.getComponentAt(index);
      tabbedPane.removeAll();
      tabbedPane.addTab(ALL, all);
    }
  }
}

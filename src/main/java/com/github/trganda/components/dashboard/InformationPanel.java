package com.github.trganda.components.dashboard;

import static javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import lombok.Getter;

@Getter
public class InformationPanel extends JPanel {
  public static final String ALL = "All";
  private JTabbedPane tabbedPane;

  public InformationPanel() {
    this.setPreferredSize(new Dimension(420, this.getPreferredSize().height));
    this.setMinimumSize(new Dimension(420, this.getPreferredSize().height));
    this.setupComponents();
  }

  private void setupComponents() {
    // Add tab with 'All' default
    this.setLayout(new BorderLayout());
    tabbedPane = new JTabbedPane();
    tabbedPane.setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
    this.addTableTab(ALL);

    this.add(tabbedPane, BorderLayout.CENTER);
  }

  public JTable addTableTab(String tabName) {
    InformationInnerPanel inner = new InformationInnerPanel();
    tabbedPane.addTab(tabName, inner);
    return inner.getInfoTable();
  }

  public int getTabComponentIndexByName(String tabName) {
    for (int i = 0; i < tabbedPane.getTabCount(); i++) {
      if (tabbedPane.getTitleAt(i).equals(tabName)) {
        return i;
      }
    }
    return -1; // Tab not found
  }

  public JTable getActiveTabView() throws RuntimeException {
    if (tabbedPane.getSelectedIndex() >= 0) {
      return ((InformationInnerPanel) tabbedPane.getSelectedComponent()).getInfoTable();
    }
    throw new RuntimeException("No active tab found.");
  }

  public String getActiveTabName() {
    if (tabbedPane.getSelectedIndex() >= 0) {
      return tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
    }
    return null;
  }

  public JTable getTabAtIndex(int index) {
    return ((InformationInnerPanel) tabbedPane.getComponentAt(index)).getInfoTable();
  }

  public void clearTab() {
    int index = getTabComponentIndexByName(ALL);
    if (index != -1) {
      int cnt = tabbedPane.getTabCount();
      java.util.List<Component> components = new ArrayList<>();
      for (int i = 0; i < cnt; i++) {
        if (i != index) {
          components.add(tabbedPane.getComponentAt(i));
        }
      }

      for (Component component : components) {
        tabbedPane.remove(component);
      }
    }
  }
}

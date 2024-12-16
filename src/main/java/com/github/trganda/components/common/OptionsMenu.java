package com.github.trganda.components.common;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.util.List;
import javax.swing.*;

public class OptionsMenu extends JPopupMenu {
  public OptionsMenu() {}

  public OptionsMenu(List<String> cols) {
    for (String col : cols) {
      JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(col, true);
      checkBoxMenuItem.putClientProperty("CheckBoxMenuItem.doNotCloseOnMouseClick", true);
      this.add(checkBoxMenuItem);
    }
    this.add(new JSeparator());
    int fontSize = UIManager.getFont("Button.font").getSize();
    FlatSVGIcon icon =
        new FlatSVGIcon(
            "svg/restore-defaults.svg", fontSize, fontSize, this.getClass().getClassLoader());
    JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem("Restore default", icon);
    this.add(checkBoxMenuItem);
  }

  //  public static class OptionsMenuItem extends JCheckBox {
  ////    private JCheckBox checkbox;
  //
  //    public OptionsMenuItem(String text, boolean selected) {
  //      super(text, selected);
  //      this.initComponents();
  //    }
  //
  //    public OptionsMenuItem(String text, Icon icon) {
  //      super(text, icon);
  //      this.initComponents();
  //    }
  //
  //    private void initComponents() {
  ////      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
  ////      this.add(checkbox);
  ////      this.setPreferredSize(new Dimension(Integer.MAX_VALUE, this.getPreferredSize().height));
  //      FindSomething.API.logging().logToOutput("OptionsMenuItem: " + this.getPreferredSize());
  //
  //      //
  // https://java-swing-tips.blogspot.com/2016/05/keep-visible-jpopupmenu-while-clicking.html
  //      this.putClientProperty("CheckBoxMenuItem.doNotCloseOnMouseClick", true);
  //      this.addMouseListener(
  //              new MouseAdapter() {
  //                @Override
  //                public void mouseEntered(MouseEvent e) {
  //                  super.mouseEntered(e);
  //                  setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
  ////                  OptionsMenuItem.this.processMouseEvent(e);
  //                }
  //
  //                @Override
  //                public void mouseExited(MouseEvent e) {
  //                  super.mouseExited(e);
  //                  setBackground(UIManager.getColor("CheckBoxMenuItem.background"));
  ////                  OptionsMenuItem.this.processMouseEvent(e);
  //                }
  //              });
  //    }
  //  }
}

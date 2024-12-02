package com.github.trganda.components.common;

import com.formdev.flatlaf.extras.components.FlatCheckBoxMenuItem;
import com.formdev.flatlaf.extras.components.FlatMenuItem;
import com.github.trganda.FindSomething;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class OptionsMenu extends JPopupMenu {
    public OptionsMenu() {
    }

    public OptionsMenu(List<String> cols) {
        for (String col : cols) {
            OptionsMenuItem checkBoxMenuItem = new OptionsMenuItem(col, true);
//            checkBoxMenuItem.setSelected(true);
            this.add(checkBoxMenuItem);
        }
        this.add(new JSeparator());
        this.add(new JMenuItem("Restore default"));
    }

    public static class OptionsMenuItem extends JCheckBoxMenuItem {
        private JCheckBox checkbox;

        public OptionsMenuItem(String text, boolean selected) {
            checkbox = new JCheckBox(text, selected);
            this.setLayout(new BorderLayout());
            this.add(checkbox);
            this.putClientProperty("CheckBoxMenuItem.doNotCloseOnMouseClick", true);
        }

//        @Override
//        public void setSelected(boolean selected) {
//            this.checkbox.setSelected(selected);
//        }
//
//        @Override
//        public boolean isSelected() {
//            return this.checkbox.isSelected();
//        }
    }

//    private static class StayOpenCheckBoxMenuItemUI extends BasicMenuItemUI {
//        @Override
//        protected void doClick(MenuSelectionManager msm) {
//            menuItem.doClick(0);
//        }
//
//        public static ComponentUI createUI(JComponent c) {
//            return new StayOpenCheckBoxMenuItemUI();
//        }
//    }
}

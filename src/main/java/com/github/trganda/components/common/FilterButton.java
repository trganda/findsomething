package com.github.trganda.components.common;

import com.formdev.flatlaf.ui.FlatButtonBorder;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FilterButton extends JButton {

  public FilterButton(String text, Icon icon) {
    super(text, icon);
    this.setHorizontalAlignment(SwingConstants.LEFT);
    this.setHorizontalTextPosition(SwingConstants.RIGHT);
    this.setBackground(UIManager.getColor("Burp.tableFilterBarBackground"));
    this.setBorderPainted(false);
    this.setBorder(new RoundedBorder(2, 8, 2, 8));
    this.getModel()
        .addChangeListener(
            e -> {
              ButtonModel model = this.getModel();
              if (model.isPressed()) {
                model.setPressed(false);
              } else if (model.isRollover()) {
                model.setRollover(false);
              }
            });
  }

  @Override
  public void updateUI() {
    super.updateUI();
    this.setBorderPainted(false);
    this.setBorder(new RoundedBorder(2, 8, 2, 8));
    this.setBackground(UIManager.getColor("Burp.tableFilterBarBackground"));
  }
}

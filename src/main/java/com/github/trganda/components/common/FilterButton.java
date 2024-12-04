package com.github.trganda.components.common;

import java.awt.*;
import javax.swing.*;

public class FilterButton extends JButton {
  public FilterButton() {
    super();
  }

  public FilterButton(String text, Icon icon) {
    super(text, icon);
    this.setHorizontalAlignment(SwingConstants.LEFT);
    this.setHorizontalTextPosition(SwingConstants.RIGHT);
    Insets insets = this.getMargin();
    insets.left = insets.left - 2;
    this.setMargin(insets);
    this.setBackground(UIManager.getColor("Burp.tableFilterBarBackground"));
    this.setBorderPainted(false);
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
    this.setBackground(UIManager.getColor("Burp.tableFilterBarBackground"));
  }
}

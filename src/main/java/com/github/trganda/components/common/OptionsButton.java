package com.github.trganda.components.common;

import javax.swing.*;

public class OptionsButton extends JButton {
  public OptionsButton() {
    super();
  }

  public OptionsButton(Icon icon) {
    super(icon);
    this.setBorderPainted(false);
    this.setBackground(UIManager.getColor("Panel.background"));
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
}

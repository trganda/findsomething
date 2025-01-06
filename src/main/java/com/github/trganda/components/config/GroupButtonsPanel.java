package com.github.trganda.components.config;

import lombok.Getter;

import java.awt.*;
import javax.swing.*;

@Getter
public class GroupButtonsPanel extends Panel {
  private final JButton remove;

  public GroupButtonsPanel() {

    remove = new JButton("Remove");

    this.setAlign(remove);
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(remove);
  }

  private void setAlign(JButton... buttons) {
    for (var button : buttons) {
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
    }
  }
}

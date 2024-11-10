package com.github.trganda.components.config;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import lombok.Getter;

@Getter
public class RuleInnerButtonsPane extends JPanel {
  private JButton add;
  private JButton edit;
  private JButton remove;
  private JButton clear;

  public RuleInnerButtonsPane() {
    add = new JButton("Add");
    edit = new JButton("Edit");
    remove = new JButton("Remove");
    clear = new JButton("Clear");

    setAlign(add, edit, remove, clear);
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(add);
    this.add(Box.createVerticalStrut(5));
    this.add(edit);
    this.add(Box.createVerticalStrut(5));
    this.add(remove);
    this.add(Box.createVerticalStrut(5));
    this.add(clear);
  }

  private void setAlign(JButton... buttons) {
    for (var button : buttons) {
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
    }
  }
}

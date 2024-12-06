package com.github.trganda.components.config;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import lombok.Getter;

@Getter
public class FilterListInnerButtonsPane extends JPanel {
  private final JComboBox<String> type;
  private final JButton remove;
  private final JButton clear;

  public static final String BLACKLIST_SUFFIX = "Suffix";
  public static final String BLACKLIST_HOST = "Host";
  public static final String BLACKLIST_STATUS = "Status";

  public FilterListInnerButtonsPane() {
    type = new JComboBox<>(new String[] {BLACKLIST_SUFFIX, BLACKLIST_HOST, BLACKLIST_STATUS});
    remove = new JButton("Remove");
    clear = new JButton("Clear");

    setAlign(remove, clear);
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(type);
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

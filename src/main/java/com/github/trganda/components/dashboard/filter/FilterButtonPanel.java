package com.github.trganda.components.dashboard.filter;

import javax.swing.*;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.github.trganda.components.common.PrimaryButton;
import lombok.Getter;

import java.awt.*;

@Getter
public class FilterButtonPanel extends JPanel {
  private final JButton apply;
  private final JButton cancel;
  private final JButton applyClose;

  public FilterButtonPanel() {
    this.apply = new JButton("Apply");
    this.cancel = new JButton("Cancel");
    this.applyClose = new PrimaryButton("Apply & Close");

    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(cancel);
    this.add(Box.createHorizontalStrut(5));
    this.add(apply);
    this.add(Box.createHorizontalStrut(5));
    this.add(applyClose);
  }
}

package com.github.trganda.components.common;

import com.formdev.flatlaf.ui.FlatButtonBorder;

import java.awt.*;

class RoundedBorder extends FlatButtonBorder {

  private final Insets insets;

  public RoundedBorder(int top, int left, int bottom, int right) {
    insets = new Insets(top, left, bottom, right);
  }

  @Override
  public Insets getBorderInsets(Component c) {
    return insets;
  }
}

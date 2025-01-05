package com.github.trganda.components.common;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTextField;
import java.awt.*;
import javax.swing.*;

public class PlaceHolderTextField extends FlatTextField {
  private FlatSVGIcon icon;
  private boolean rounded;

  public PlaceHolderTextField(String placeHolder) {
    this(placeHolder, null, false);
  }

  public PlaceHolderTextField(String placeHolder, FlatSVGIcon icon) {
    this(placeHolder, icon, false);
  }

  public PlaceHolderTextField(String placeHolder, FlatSVGIcon icon, boolean rounded) {
    this.rounded = rounded;
    this.icon = icon;
    if (rounded) {
      this.setBorder(new RoundedBorder(2, 8, 2, 8));
    }
    this.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeHolder);

    if (icon != null) {
      this.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, icon);
    }
    this.updateUI();
  }

  @Override
  public void updateUI() {
    super.updateUI();
    if (this.icon != null) {
      int fontSize = UIManager.getFont("TitlePane.small.font").getSize();
      FlatSVGIcon svgIcon =
          new FlatSVGIcon(this.icon.getName(), fontSize, fontSize, this.icon.getClassLoader());
      svgIcon = svgIcon.derive(1.2f);
      svgIcon.setColorFilter(
          new FlatSVGIcon.ColorFilter(
              c -> {
                if (c.getRGB() == Color.BLACK.getRGB()) {
                  return UIManager.getColor("Burp.buttonForeground");
                }
                return c;
              }));
      this.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, svgIcon);
    }

    if (rounded) {
      this.setBorder(new RoundedBorder(2, 8, 2, 8));
    }
  }
}

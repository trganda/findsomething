package com.github.trganda.components.common;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTextField;
import java.awt.*;
import javax.swing.*;

public class RoundTextField extends FlatTextField {

  public RoundTextField(String placeholder) {
    this.setBorder(new RoundedBorder(2, 8, 2, 8));
    this.setPreferredSize(new Dimension(260, this.getPreferredSize().height));
    this.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
  }

  @Override
  public void updateUI() {
    super.updateUI();
    int fontSize = UIManager.getFont("TitlePane.small.font").getSize();

    FlatSVGIcon icon =
        new FlatSVGIcon("svg/search.svg", fontSize, fontSize, this.getClass().getClassLoader());
    icon = icon.derive(1.2f);
    icon.setColorFilter(
        new FlatSVGIcon.ColorFilter(
            c -> {
              if (c.getRGB() == Color.BLACK.getRGB()) {
                return UIManager.getColor("Burp.buttonForeground");
              }
              return c;
            }));
    this.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, icon);
    this.setBorder(new RoundedBorder(2, 8, 2, 8));
  }
}

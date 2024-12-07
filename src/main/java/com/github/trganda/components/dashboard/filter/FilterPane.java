package com.github.trganda.components.dashboard.filter;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.github.trganda.FindSomething;
import com.github.trganda.components.common.FilterButton;
import com.github.trganda.components.common.OptionsButton;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class FilterPane extends JPanel {

  private JButton filterButton;
  private JButton optionsButton;
  private int fontSize = UIManager.getFont("Button.font").getSize();
  private FlatSVGIcon filterIcon;
  private FlatSVGIcon optionsIcon;

  public FilterPane() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupComponents() {}

  private void setupLayout() {
    this.setLayout(new BorderLayout());
    this.add(filterButton, BorderLayout.CENTER);
    this.add(optionsButton, BorderLayout.EAST);
  }

  @Override
  public void updateUI() {
    super.updateUI();
    fontSize = UIManager.getFont("Button.font").getSize();
    filterIcon =
        new FlatSVGIcon("svg/filter.svg", fontSize, fontSize, this.getClass().getClassLoader());
    filterIcon = filterIcon.derive(1.2f);
    filterIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> {
      if (c.getRGB() == Color.BLACK.getRGB()) {
        return UIManager.getColor("Burp.buttonForeground");
      }
      return c;
    }));

    optionsIcon =
        new FlatSVGIcon(
            "svg/options.svg",
            fontSize,fontSize,
            this.getClass().getClassLoader());

    optionsIcon = optionsIcon.derive(1.5f);
    optionsIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> {
      if (c.getRGB() == Color.BLACK.getRGB()) {
        return UIManager.getColor("Burp.buttonForeground");
      }
      return c;
    }));

    if (filterButton != null) {
      filterButton.setIcon(filterIcon);
    } else {
      filterButton = new FilterButton("Filter", filterIcon);
    }

    if (optionsButton != null) {
      optionsButton.setIcon(optionsIcon);
    } else {
      optionsButton = new OptionsButton(optionsIcon);
    }
//    filterButton = new FilterButton("Filter", filterIcon);
//    optionsButton = new OptionsButton(optionsIcon);
  }
}

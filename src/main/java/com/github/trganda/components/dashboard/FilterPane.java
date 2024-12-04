package com.github.trganda.components.dashboard;

import com.formdev.flatlaf.extras.FlatSVGIcon;
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
  private FlatSVGIcon filterIcon =
      new FlatSVGIcon("svg/filter.svg", fontSize, fontSize, this.getClass().getClassLoader());
  ;
  private FlatSVGIcon optionsIcon =
      new FlatSVGIcon("svg/options.svg", fontSize, fontSize, this.getClass().getClassLoader());

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
    optionsIcon =
        new FlatSVGIcon("svg/options.svg", fontSize, fontSize, this.getClass().getClassLoader());

    filterButton = new FilterButton("Filter", filterIcon);
    optionsButton = new OptionsButton(optionsIcon);
  }
}

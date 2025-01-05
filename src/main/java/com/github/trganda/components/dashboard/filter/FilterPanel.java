package com.github.trganda.components.dashboard.filter;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.github.trganda.components.common.FilterButton;
import com.github.trganda.components.common.OptionsButton;
import com.github.trganda.components.common.PlaceHolderTextField;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import lombok.Getter;

@Getter
public class FilterPanel extends JPanel {

  private JButton filterButton;
  private JButton optionsButton;
  private FlatSVGIcon filterIcon;
  private FlatSVGIcon optionsIcon;
  private final JTextField search;
  private int fontSize = UIManager.getFont("TitlePane.small.font").getSize();
  private static final Border border = new JButton().getBorder();

  public FilterPanel() {
    FlatSVGIcon icon =
        new FlatSVGIcon("svg/search.svg", fontSize, fontSize, this.getClass().getClassLoader());
    search = new PlaceHolderTextField("Search", icon, true);
    //    search = new RoundSearchTextField("Search");
    search.setPreferredSize(new Dimension(260, search.getPreferredSize().height));
    this.setupLayout();
  }

  private void setupLayout() {
    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 0, 10);
    this.add(filterButton, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    this.add(search, gbc);

    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(optionsButton, gbc);
  }

  @Override
  public void updateUI() {
    super.updateUI();
    fontSize = UIManager.getFont("TitlePane.small.font").getSize();
    filterIcon =
        new FlatSVGIcon("svg/filter.svg", fontSize, fontSize, this.getClass().getClassLoader());
    filterIcon = filterIcon.derive(1.2f);
    filterIcon.setColorFilter(
        new FlatSVGIcon.ColorFilter(
            c -> {
              if (c.getRGB() == Color.BLACK.getRGB()) {
                return UIManager.getColor("Burp.buttonForeground");
              }
              return c;
            }));

    optionsIcon =
        new FlatSVGIcon("svg/options.svg", fontSize, fontSize, this.getClass().getClassLoader());

    optionsIcon = optionsIcon.derive(1.5f);
    optionsIcon.setColorFilter(
        new FlatSVGIcon.ColorFilter(
            c -> {
              if (c.getRGB() == Color.BLACK.getRGB()) {
                return UIManager.getColor("Burp.buttonForeground");
              }
              return c;
            }));

    if (filterButton != null) {
      filterButton.setIcon(filterIcon);
    } else {
      filterButton = new FilterButton("Filter settings", filterIcon);
    }

    if (optionsButton != null) {
      optionsButton.setIcon(optionsIcon);
    } else {
      optionsButton = new OptionsButton(optionsIcon);
    }
  }
}

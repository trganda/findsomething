package com.github.trganda.components.dashboard;

import static org.apache.batik.transcoder.XMLAbstractTranscoder.*;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.github.trganda.FindSomething;
import com.github.trganda.components.common.FilterButton;
import com.github.trganda.components.common.OptionsButton;
import com.github.trganda.components.common.OptionsMenu;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import lombok.Getter;
import org.apache.batik.transcoder.*;

@Getter
public class FilterPane extends javax.swing.JPanel {
  private HostFilterPane hostFilter;
  private InformationFilterPane informationFilter;
  private JButton filterButton;
  private JButton optionsButton;

  public FilterPane() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupComponents() {
    int fontSize = UIManager.getFont("Button.font").getSize();
    FlatSVGIcon filterIcon =
        new FlatSVGIcon(
            "svg/filter.svg", fontSize + 2, fontSize + 2, this.getClass().getClassLoader());
    filterButton = new FilterButton("Filter", filterIcon);

    FlatSVGIcon optionsIcon =
        new FlatSVGIcon(
            "svg/options.svg", fontSize + 2, fontSize + 2, this.getClass().getClassLoader());

    OptionsMenu optionsMenu =
        new OptionsMenu(Arrays.asList("#", "Method", "URL", "Referer", "Status"));

    optionsButton = new OptionsButton(optionsIcon);
    optionsButton.addActionListener(
        e -> {
          FindSomething.API.userInterface().applyThemeToComponent(optionsMenu);
          optionsMenu.show(optionsButton, 0, 0);
        });
  }

  private void setupLayout() {
    this.setLayout(new BorderLayout());
    this.add(filterButton, BorderLayout.CENTER);

    this.add(optionsButton, BorderLayout.EAST);
    // GridBagLayout layout = new GridBagLayout();
    // GridBagConstraints gbc = new GridBagConstraints();

    // this.setLayout(layout);

    // gbc.gridx = 0;
    // gbc.gridy = 0;
    // gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    // gbc.insets = new Insets(0, 0, 0, 5);
    // this.add(hostFilter, gbc);

    // gbc.gridx = 1;
    // gbc.gridy = 0;
    // gbc.insets = new Insets(0, 0, 0, 0);
    // this.add(informationFilter, gbc);
  }
}

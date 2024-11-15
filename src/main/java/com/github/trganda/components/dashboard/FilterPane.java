package com.github.trganda.components.dashboard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import lombok.Getter;

@Getter
public class FilterPane extends JPanel {
  private HostFilterPane hostFilter;
  private InformationFilterPane informationFilter;

  public FilterPane() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupComponents() {
    hostFilter = new HostFilterPane();
    informationFilter = new InformationFilterPane();
  }

  private void setupLayout() {
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.insets = new Insets(0, 0, 0, 5);
    this.add(hostFilter, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(informationFilter, gbc);
  }
}

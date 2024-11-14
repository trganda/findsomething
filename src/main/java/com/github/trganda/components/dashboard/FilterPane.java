package com.github.trganda.components.dashboard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import lombok.Getter;

@Getter
public class FilterPane extends JPanel {
  private RequestFilterPane requestFilter;
  private InformationFilterPane informationFilter;

  public FilterPane() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupComponents() {
    requestFilter = new RequestFilterPane();
    informationFilter = new InformationFilterPane();
  }

  private void setupLayout() {
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(informationFilter, gbc);

    // gbc.gridx = 1;
    // gbc.gridy = 0;
    // this.add(requestFilter, gbc);
  }
}

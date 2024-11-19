package com.github.trganda.components.dashboard;

import com.github.trganda.components.common.SuggestionComboBox;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import lombok.Getter;

@Getter
public class HostFilterPane extends JPanel {
  private SuggestionComboBox suggestion;

  public HostFilterPane() {
    this.setupComponents();
    this.setupLayout();
    this.setBorder(new TitledBorder("Filter by host"));
  }

  private void setupComponents() {
    suggestion = new SuggestionComboBox();
  }

  private void setupLayout() {
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.insets = new Insets(0, 2, 5, 5);
    this.add(new JLabel("Host:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 5, 2);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(suggestion, gbc);
  }
}

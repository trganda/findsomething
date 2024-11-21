package com.github.trganda.components.dashboard;

import static com.github.trganda.config.Config.GROUP_FINGERPRINT;
import static com.github.trganda.config.Config.GROUP_GENERAL;
import static com.github.trganda.config.Config.GROUP_INFORMATION;
import static com.github.trganda.config.Config.GROUP_SENSITIVE;
import static com.github.trganda.config.Config.GROUP_VULNERABILITY;

import com.github.trganda.components.common.SuggestionComboBox;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import lombok.Getter;

@Getter
public class HostFilterPane extends JPanel {
  private SuggestionComboBox suggestion;
  private JComboBox<String> selector;

  public HostFilterPane() {
    this.setupComponents();
    this.setupLayout();
    this.setBorder(new TitledBorder("Filter by host"));
  }

  private void setupComponents() {
    suggestion = new SuggestionComboBox();
    selector =
        new JComboBox<>(
            new String[] {
              GROUP_GENERAL,
              GROUP_FINGERPRINT,
              GROUP_SENSITIVE,
              GROUP_VULNERABILITY,
              GROUP_INFORMATION
            });
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

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(0, 2, 5, 5);
    this.add(new JLabel("Group:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.insets = new Insets(0, 0, 5, 2);
    this.add(selector, gbc);
  }
}

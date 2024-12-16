package com.github.trganda.controller.dashboard;

import com.github.trganda.components.common.OptionsMenu;
import java.util.Arrays;
import javax.swing.*;

public class OptionsButtonController {

  private JButton optionsButton;
  private OptionsMenu optionsMenu;

  public OptionsButtonController(JButton optionsButton) {
    this.optionsButton = optionsButton;
    this.optionsMenu = new OptionsMenu(Arrays.asList("#", "Method", "URL", "Referer", "Status"));
    this.setupEventListener();
  }

  private void setupEventListener() {
    optionsButton.addActionListener(
        e -> {
          optionsMenu.show(optionsButton, 0, 0);
        });
  }
}

package com.github.trganda.controller.dashboard;

import com.github.trganda.FindSomething;
import com.github.trganda.components.common.OptionsMenu;
import java.util.Arrays;
import javax.swing.*;

public class OptionsButtonController {

  private JButton optionsButton;

  public OptionsButtonController(JButton optionsButton) {
    this.optionsButton = optionsButton;
    this.setupEventListener();
  }

  private void setupEventListener() {
    OptionsMenu optionsMenu =
        new OptionsMenu(Arrays.asList("#", "Method", "URL", "Referer", "Status"));
    optionsButton.addActionListener(
        e -> {
          FindSomething.API.userInterface().applyThemeToComponent(optionsMenu);
          optionsMenu.show(optionsButton, 0, 0);
        });
  }
}

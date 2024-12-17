package com.github.trganda.components.common;

import javax.swing.*;

public class PrimaryButton extends JButton {
  public PrimaryButton(String text) {
    super(text);
    this.setBorderPainted(false);
    this.setFont(UIManager.getFont("h4.font"));
    this.setForeground(UIManager.getColor("Burp.primaryButtonForeground"));
    this.setBackground(UIManager.getColor("Burp.primaryButtonBackground"));
  }

  @Override
  public void updateUI() {
    super.updateUI();
    this.setBorderPainted(false);
    this.setFont(UIManager.getFont("h4.font"));
    this.setForeground(UIManager.getColor("Burp.primaryButtonForeground"));
    this.setBackground(UIManager.getColor("Burp.primaryButtonBackground"));
  }
}

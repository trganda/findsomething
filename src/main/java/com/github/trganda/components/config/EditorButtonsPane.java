package com.github.trganda.components.config;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.trganda.components.common.PrimaryButton;
import lombok.Getter;

@Getter
public class EditorButtonsPane extends JPanel {

  private JButton cancel;
  private JButton save;

  public EditorButtonsPane() {
    cancel = new JButton("Cancel");
    save = new PrimaryButton("Save");

    this.setAlign(cancel, save);
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(cancel);
    this.add(Box.createHorizontalStrut(5));
    this.add(save);
  }

  private void setAlign(JButton... buttons) {
    for (var button : buttons) {
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
  }
}

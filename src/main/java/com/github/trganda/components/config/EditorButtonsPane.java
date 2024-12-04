package com.github.trganda.components.config;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import lombok.Getter;

@Getter
public class EditorButtonsPane extends JPanel {

  private JButton cancel;
  private JButton save;

  public EditorButtonsPane() {
    cancel = new JButton("Cancel");
    save = new JButton("Save");
    //    save.setBackground(new Color(230, 93, 50));
    //    save.setForeground(Color.WHITE);
    //    save.setBorderPainted(false);
    //    save.setFont(
    //        new Font(
    //            Utils.getBurpDisplayFont().getName(), Font.BOLD,
    // Utils.getBurpDisplayFont().getSize()));

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

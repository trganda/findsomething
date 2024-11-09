package com.github.trganda.components.config;

import com.github.trganda.utils.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import lombok.Getter;

@Getter
public class EditroButtonsPane extends JPanel {

  private JButton cancel;
  private JButton save;

  public EditroButtonsPane() {
    cancel = new JButton("Cancel");
    save = new JButton("Save");
    save.setBackground(new Color(230, 93, 50));
    save.setForeground(Color.WHITE);
    save.setBorderPainted(false);
    save.setFont(
        new Font(
            Utils.getBurpDisplayFont().getName(), Font.BOLD, Utils.getBurpDisplayFont().getSize()));

    this.setAlign(cancel, save);
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(cancel);
    this.add(Box.createHorizontalStrut(5));
    this.add(save);

    // this.setupButtonEventHandler();
  }

  private void setAlign(JButton... buttons) {
    for (var button : buttons) {
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
  }

  // private void setupButtonEventHandler() {
  //   cancel.addActionListener(
  //       e -> {
  //         Editor.this.setVisible(false);
  //       });

  //   save.addActionListener(
  //       e -> {
  //         Rule r =
  //             new Rule(
  //                 true,
  //                 nameField.getText(),
  //                 regexField.getText(),
  //                 (Scope) scope.getSelectedItem(),
  //                 sensitive.isSelected());
  //         if (op == Operatation.ADD) {
  //           Config.getInstance().syncRules(group, r, op);
  //         } else if (op == Operatation.EDT) {
  //           // remove the old rule first
  //           Config.getInstance().syncRules(group, rule, Operatation.DEL);
  //           Config.getInstance().syncRules(group, r, op);
  //         }
  //         Editor.this.setVisible(false);
  //       });
  // }
}

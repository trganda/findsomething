package com.github.trganda.components;

import com.github.trganda.utils.Utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

public class UIRender {

  public static JTextField renderTextField(JTextField textField, String placeHolder) {
    textField.setFont(
        new Font(
            Utils.getBurpDisplayFont().getName(),
            Font.PLAIN,
            Utils.getBurpDisplayFont().getSize()));
    textField.setForeground(Color.GRAY);
    textField.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent e) {
            super.focusGained(e);
            if (textField.getText().equals(placeHolder)) {
              textField.setFont(
                  new Font(
                      Utils.getBurpDisplayFont().getName(),
                      Font.PLAIN,
                      Utils.getBurpDisplayFont().getSize()));
              textField.setForeground(Color.BLACK);
              textField.setText("");
            }
          }

          @Override
          public void focusLost(FocusEvent e) {
            super.focusLost(e);
            if (textField.getText().isEmpty()) {
              textField.setFont(
                  new Font(
                      Utils.getBurpDisplayFont().getName(),
                      Font.ITALIC,
                      Utils.getBurpDisplayFont().getSize()));
              textField.setForeground(Color.GRAY);
              textField.setText(placeHolder);
            }
          }
        });
    return textField;
  }
}

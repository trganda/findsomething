package com.github.trganda.components.common;

import com.github.trganda.utils.Utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import lombok.Getter;

@Getter
public class PlaceHolderFocusListener extends FocusAdapter {

  private final String placeHolder;
  private final JTextField textField;
  private boolean isPlaceholderActive;

  public PlaceHolderFocusListener(JTextField textField, String placeHolder) {
    this.textField = textField;
    this.placeHolder = placeHolder;
    this.isPlaceholderActive = true;
    textField.setFont(
        new Font(
            Utils.getBurpDisplayFont().getName(),
            Font.ITALIC,
            Utils.getBurpDisplayFont().getSize()));
    textField.setForeground(Color.GRAY);
    textField.setText(placeHolder);
  }

  @Override
  public void focusGained(FocusEvent e) {
    super.focusGained(e);
    if (isPlaceholderActive) {
      textField.setFont(
          new Font(
              Utils.getBurpDisplayFont().getName(),
              Font.PLAIN,
              Utils.getBurpDisplayFont().getSize()));
      textField.setForeground(Color.BLACK);
      textField.setText("");
      isPlaceholderActive = false;
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
      isPlaceholderActive = true;
    }
  }
}

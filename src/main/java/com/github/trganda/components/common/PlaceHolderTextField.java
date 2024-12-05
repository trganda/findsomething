package com.github.trganda.components.common;

import javax.swing.JTextField;
import lombok.Getter;

@Getter
public class PlaceHolderTextField extends JTextField {

  private final String placeHolder;
  private final PlaceHolderFocusListener placeHolderFocusListener;

  public PlaceHolderTextField(String placeHolder) {
    this.placeHolder = placeHolder;
    this.placeHolderFocusListener = new PlaceHolderFocusListener(this, placeHolder);
    this.addFocusListener(placeHolderFocusListener);
  }

  public boolean isPlaceholderActive() {
    return this.placeHolderFocusListener.isPlaceholderActive();
  }

  public String getSelectedText() {
    if (this.isPlaceholderActive()) {
      return "";
    }
    return this.getText();
  }
}

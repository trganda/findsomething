package com.github.trganda.components.common;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import lombok.Getter;

@Getter
public class SuggestionKeyListener extends KeyAdapter {

  private SuggestionComboBox suggestionComboBox;
  private JTextField textField;
  private JComboBox<String> comboBox;
  private DefaultComboBoxModel<String> model;

  public SuggestionKeyListener(SuggestionComboBox suggestionComboBox) {
    this.suggestionComboBox = suggestionComboBox;
    this.comboBox = suggestionComboBox.getHostComboBox();
    this.model = suggestionComboBox.getHostComboBoxModel();
    this.textField = suggestionComboBox.getHostTextField();
  }

  @Override
  public void keyPressed(KeyEvent e) {
    this.suggestionComboBox.setMatching(true);
    int keyCode = e.getKeyCode();
    switch (keyCode) {
      case KeyEvent.VK_DOWN:
      case KeyEvent.VK_UP:
        if (comboBox.isPopupVisible() && !e.isConsumed()) {
          comboBox.dispatchEvent(e);
        }
        break;
      case KeyEvent.VK_ENTER:
        this.suggestionComboBox.setMatching(false);
        if (!suggestionComboBox.isMatching() && comboBox.getSelectedIndex() >= 0) {
          String selectedHost = comboBox.getSelectedItem().toString();
          textField.setText(selectedHost);
          comboBox.setPopupVisible(false);
        }
        break;
      case KeyEvent.VK_ESCAPE:
        comboBox.setPopupVisible(false);
      default:
        break;
    }
    this.suggestionComboBox.setMatching(false);
  }
}

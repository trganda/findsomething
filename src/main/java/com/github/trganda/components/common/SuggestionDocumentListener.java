package com.github.trganda.components.common;

import com.github.trganda.utils.cache.CachePool;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SuggestionDocumentListener implements DocumentListener {

  private JTextField textField;
  private JComboBox<String> comboBox;
  private DefaultComboBoxModel<String> model;
  private SuggestionComboBox suggestionComboBox;

  public SuggestionDocumentListener(SuggestionComboBox suggestionComboBox) {
    this.suggestionComboBox = suggestionComboBox;
    this.textField = suggestionComboBox.getHostTextField();
    this.comboBox = suggestionComboBox.getHostComboBox();
    this.model = suggestionComboBox.getHostComboBoxModel();
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    updateSuggestions();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    updateSuggestions();
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    updateSuggestions();
  }

  private void updateSuggestions() {
    suggestionComboBox.setMatching(true);
    String input = textField.getText().toLowerCase();
    List<String> hosts = CachePool.getInstance().getHosts();

    // Clear existing items and add new suggestions based on the input
    model.removeAllElements();
    if (!input.isEmpty()) {
      for (String suggestion : hosts) {
        if (suggestion.toLowerCase().contains(input)) {
          if (suggestion.toLowerCase().equals(input)) {
            model.insertElementAt(input, 0);
            model.setSelectedItem(input);
          } else {
            model.addElement(suggestion);
          }
        }
      }
    }

    // Show the popup if there are suggestions
    if (model.getSize() > 0) {
      comboBox.setPopupVisible(true);
    } else {
      comboBox.setPopupVisible(false);
    }
    suggestionComboBox.setMatching(false);
  }
}

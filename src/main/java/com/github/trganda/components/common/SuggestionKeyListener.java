package com.github.trganda.components.common;

import com.github.trganda.FindSomething;
import com.github.trganda.utils.cache.CachePool;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SuggestionKeyListener extends KeyAdapter {

  private JTextField editor;
  private SuggestionCombox<String> hostComboBox;
  private DefaultComboBoxModel<String> model;
  private List<String> suggestions;

  public SuggestionKeyListener(
      SuggestionCombox<String> comboBox, DefaultComboBoxModel<String> model) {
    this.hostComboBox = comboBox;
    this.model = model;
    this.editor = (JTextField) comboBox.getEditor().getEditorComponent();
  }

  @Override
  public void keyReleased(KeyEvent e) {
    this.suggestions = CachePool.getInstance().getHosts();
    this.suggestions.stream().forEach(FindSomething.API.logging()::logToOutput);

    updateSuggestions();
  }

  private void updateSuggestions() {
    String input = editor.getText();

    // Clear existing items and add new suggestions based on the input
    this.model.removeAllElements();
    if (!input.isEmpty()) {
      for (String suggestion : suggestions) {
        if (suggestion.contains(input.toLowerCase())) {
          this.model.addElement(suggestion);
        }
      }
    }

    // Show the popup if there are suggestions
    if (this.model.getSize() > 0) {
      hostComboBox.setPopupVisible(true);
    } else {
      hostComboBox.setPopupVisible(false);
    }

    // Reset the editor text without setting a selected item
    this.editor.setText(input);
  }
}

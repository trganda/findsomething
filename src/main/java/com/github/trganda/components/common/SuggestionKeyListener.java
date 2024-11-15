package com.github.trganda.components.common;

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

  // Identify whether the combox was popup
  private boolean popup = false;

  // Tracing the position of selected item
  private int selectedIdx = -1;

  public SuggestionKeyListener(
      SuggestionCombox<String> comboBox, DefaultComboBoxModel<String> model) {
    this.hostComboBox = comboBox;
    this.model = model;
    this.editor = (JTextField) comboBox.getEditor().getEditorComponent();
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.isControlDown() || e.isAltDown() || e.isMetaDown() || e.isAltGraphDown()) {
      return;
    }

    int keyCode = e.getKeyCode();
    switch (keyCode) {
      case KeyEvent.VK_UP:
        // Move selection up if possible
        int prevIndex = hostComboBox.getSelectedIndex() - 1;
        if (prevIndex >= 0) {
          hostComboBox.setSelectedIndex(prevIndex);
          selectedIdx = prevIndex;
        }

        break;

      case KeyEvent.VK_DOWN:
        // Move selection down if possible
        int nextIndex = hostComboBox.getSelectedIndex() + 1;
        if (nextIndex < model.getSize()) {
          hostComboBox.setSelectedIndex(nextIndex);
          selectedIdx = nextIndex;
        }

        break;

      case KeyEvent.VK_ENTER:
        // Confirm selection and close popup, have to use the a self-hosted flag to check the status
        // of combox in KeyEvent.VK_ENTER, since the in the event the combox.getSelectedIndex()
        // always will return -1.
        if (popup && selectedIdx > -1) {
          editor.setText(model.getElementAt(selectedIdx));
          hostComboBox.setPopupVisible(false);
          popup = false;
          selectedIdx = -1;
        }
        break;

      default:
        // Check if key is a printable character
        if (keyCode == KeyEvent.VK_DELETE
            || keyCode == KeyEvent.VK_BACK_SPACE
            || isPrintableCharacter(e.getKeyChar())) {
          updateSuggestions();
        }
        break;
    }
  }

  private boolean isPrintableCharacter(char c) {
    // Printable characters fall in the range of visible ASCII or Unicode symbols
    return !Character.isISOControl(c) && c != KeyEvent.CHAR_UNDEFINED;
  }

  private void updateSuggestions() {
    String input = editor.getText();
    this.suggestions = CachePool.getInstance().getHosts();

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
      popup = true;
    } else {
      hostComboBox.setPopupVisible(false);
      popup = false;
      selectedIdx = -1;
    }

    // Reset the editor text without setting a selected item
    this.editor.setText(input);
  }
}

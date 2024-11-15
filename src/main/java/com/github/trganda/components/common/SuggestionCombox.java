package com.github.trganda.components.common;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import lombok.Getter;

@Getter
public class SuggestionCombox<T> extends JComboBox<T> {

  public SuggestionCombox(ComboBoxModel<T> aModel) {
    super(aModel);
    this.setEditable(true);
    // Custom UI to hide arrow button
    this.setUI(
        new BasicComboBoxUI() {
          @Override
          protected JButton createArrowButton() {
            return new JButton() {
              @Override
              public int getWidth() {
                return 0; // Hide button by setting width to 0
              }
            };
          }
        });

    // Remove arrow button component
    this.remove(this.getComponent(0));
    this.setPreferredSize(new JTextField().getPreferredSize());
    ((JComponent) this.getEditor().getEditorComponent()).setBorder(new EmptyBorder(2, 5, 2, 5));
  }
}

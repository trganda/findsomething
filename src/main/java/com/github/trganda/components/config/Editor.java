package com.github.trganda.components.config;

import com.github.trganda.config.Rules.Rule;
import com.github.trganda.utils.Utils;
import java.awt.*;
import javax.swing.*;

public class Editor extends JDialog {

  private JTextField nameField;
  private JTextField regexField;
  private JCheckBox sensitive;

  private Rule rule;

  public Editor() {}

  public Editor(Frame pFrame) {
    super(pFrame);
    this.setLocationRelativeTo(pFrame);
    this.setLayout(new GridBagLayout());
    this.setMinimumSize(new Dimension(400, 180));
    this.setMaximumSize(new Dimension(400, 180));

    nameField = new JTextField();
    nameField.setPreferredSize(new Dimension(300, nameField.getPreferredSize().height));
    regexField = new JTextField();
    regexField.setPreferredSize(new Dimension(300, regexField.getPreferredSize().height));
    sensitive = new JCheckBox();

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.insets = new Insets(0, 0, 5, 5);
    JLabel namLabel = new JLabel("Name:");
    this.add(namLabel, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 5, 0);

    this.add(nameField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(0, 0, 5, 5);
    this.add(new JLabel("Regex:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.insets = new Insets(0, 0, 5, 0);

    this.add(regexField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(new JLabel("Sensitive:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    this.add(sensitive, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.insets = new Insets(10, 0, 0, 0);
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.LINE_END;
    this.add(new EditroButtonsPane(), gbc);
  }

  public Editor(Frame pFrame, Rule pRule) {
    this(pFrame);
    this.rule = pRule;
    this.nameField.setText(pRule.getName());
    this.regexField.setText(pRule.getRegex());
    this.sensitive.setSelected(pRule.isSensitive());
  }

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
              Utils.getBurpDisplayFont().getName(),
              Font.BOLD,
              Utils.getBurpDisplayFont().getSize()));

      this.setAlign(cancel, save);
      this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      this.add(cancel);
      this.add(Box.createHorizontalStrut(5));
      this.add(save);

      this.setupButtonEventHandler();
    }

    private void setAlign(JButton... buttons) {
      for (var button : buttons) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
      }
    }

    private void setupButtonEventHandler() {
      cancel.addActionListener(
          e -> {
            Editor.this.setVisible(false);
          });
    }
  }
}

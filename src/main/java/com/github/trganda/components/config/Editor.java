package com.github.trganda.components.config;

import com.github.trganda.config.Operation;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.config.Scope;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Editor extends JDialog {

  private Frame pFrame;
  private JTextField nameField;
  private JTextField regexField;
  private JTextField groupField;
  private JComboBox<Scope> scope;
  private JCheckBox sensitive;
  private EditorButtonsPane editorButtonsPane;

  private String group;
  private Operation op;

  private Rule rule;

  public Editor() {}

  public Editor(Frame pFrame) {
    super(pFrame);
    this.pFrame = pFrame;

    nameField = new JTextField();
    nameField.setPreferredSize(new Dimension(300, nameField.getPreferredSize().height));
    regexField = new JTextField();
    regexField.setPreferredSize(new Dimension(300, regexField.getPreferredSize().height));
    sensitive = new JCheckBox();
    editorButtonsPane = new EditorButtonsPane();
    scope = new JComboBox<>(Scope.values());

    setupLayout();
  }

  public Editor(Frame pFrame, String group) {
    this(pFrame);
    this.group = group;
    this.op = Operation.ADD;
  }

  private void setupLayout() {
    this.setLayout(new GridBagLayout());
    this.setMinimumSize(new Dimension(400, 180));
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 5, 5);
    this.add(new JLabel("Name:"), gbc);

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
    gbc.insets = new Insets(0, 0, 5, 5);
    this.add(new JLabel("Scope:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.insets = new Insets(0, 0, 5, 0);
    this.add(scope, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(new JLabel("Sensitive:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    this.add(sensitive, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.insets = new Insets(10, 0, 0, 0);
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.NONE;
    this.add(editorButtonsPane, gbc);
  }

  public void setRule(Rule rule) {
    this.rule = rule;
    nameField.setText(rule.getName());
    regexField.setText(rule.getRegex());
    sensitive.setSelected(rule.isSensitive());
    scope.setSelectedItem(rule.getScope());
  }
}

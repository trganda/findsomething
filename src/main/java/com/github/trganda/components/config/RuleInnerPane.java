package com.github.trganda.components.config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static com.github.trganda.config.Config.*;

public class RuleInnerPane extends JPanel {

    private RuleButtonsPane ruleButtonsPane;
    private JComboBox<String> selector;
    private JTable table;
    private DefaultTableModel model;

    public RuleInnerPane() {
        ruleButtonsPane = new RuleButtonsPane();
        selector = new JComboBox<>(new String[]{GROUP_FINGERPRINT, GROUP_SENSITIVE, GROUP_VULNERABILITY, GROUP_INFORMATION});
        model = new DefaultTableModel(new Object[]{"Enabled", "Name", "Regex", "Sensitive"}, 0);
        table = new JTable(model);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(layout);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel label = new JLabel("Rule type:");
        this.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        selector.setPreferredSize(new Dimension(160, selector.getPreferredSize().height));
        this.add(selector, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 0, 20, 0);
        JSeparator separator = new JSeparator();
        this.add(separator, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 5, 5);
        this.add(ruleButtonsPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(setupTable(), gbc);
    }

    private JComponent setupTable() {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(table.getPreferredSize().width, 160));
        return scrollPane;
    }

    private class RuleButtonsPane extends JPanel {
        private JButton add;
        private JButton remove;
        private JButton clear;

        public RuleButtonsPane() {
            add = new JButton("Add");
            remove = new JButton("Remove");
            clear = new JButton("Clear");

            setAlign(add, remove, clear);
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(add);
            this.add(Box.createVerticalStrut(5));
            this.add(remove);
            this.add(Box.createVerticalStrut(5));
            this.add(clear);
        }

        private void setAlign(JButton... buttons) {
            for (var button : buttons) {
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            }
        }
    }
}

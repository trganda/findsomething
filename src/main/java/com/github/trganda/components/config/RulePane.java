package com.github.trganda.components.config;

import javax.swing.*;
import java.awt.*;

public class RulePane extends JPanel {

    private JLabel label;
    private JLabel description;
    private JTabbedPane tabbedPane;

    public RulePane() {
        label = new JLabel("Rules set");
        label.setFont(new Font("Arial", Font.BOLD, 16));

        description = new JLabel("description");

        tabbedPane = new JTabbedPane();
        tabbedPane.add("Information", new RuleTypePane());
        tabbedPane.setMaximumSize(tabbedPane.getPreferredSize());

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(label);
        this.add(description);
        this.add(tabbedPane);
    }
}

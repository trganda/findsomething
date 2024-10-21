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
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        description = new JLabel("description");
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        tabbedPane = new JTabbedPane();
        tabbedPane.add("Information", new RuleTypePane());
        tabbedPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabbedPane.setMaximumSize(tabbedPane.getPreferredSize());

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(label);
        this.add(description);
        this.add(tabbedPane);
    }
}

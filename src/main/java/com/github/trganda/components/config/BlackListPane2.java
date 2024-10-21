package com.github.trganda.components.config;

import javax.swing.*;
import java.awt.*;

public class BlackListPane2 extends JPanel {

    private JLabel label;

    private JLabel description;

    private BlackListButtonsPane blackListButtonsPane;

    public BlackListPane2() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        blackListButtonsPane = new BlackListButtonsPane();
        label = new JLabel("Ignore setting");
        label.setAlignmentX(LEFT_ALIGNMENT);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        description = new JLabel("Description");
        description.setAlignmentX(LEFT_ALIGNMENT);
        blackListButtonsPane.setAlignmentX(LEFT_ALIGNMENT);

        this.add(label);
        this.add(description);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
        buttonPanel.add(blackListButtonsPane);
        this.add(buttonPanel);
    }
}

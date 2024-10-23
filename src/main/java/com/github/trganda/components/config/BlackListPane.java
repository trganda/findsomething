package com.github.trganda.components.config;

import javax.swing.*;
import java.awt.*;

public class BlackListPane extends JPanel {

    private JLabel label;
    private JLabel description;
    private BlackListInnerPane blackListInnerPane;

    public BlackListPane() {
        blackListInnerPane = new BlackListInnerPane();
        label = new JLabel("Blacklist setting");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        description = new JLabel("You can set different type black list to ignore while grep for information.");

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(gridBagLayout);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 15, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        this.add(label, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(description, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(blackListInnerPane, gbc);
    }

}

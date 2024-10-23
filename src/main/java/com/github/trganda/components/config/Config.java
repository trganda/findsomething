package com.github.trganda.components.config;

import javax.swing.*;
import java.awt.*;

public class Config extends JPanel {

    private RulePane rulePane;

    private BlackListPane blackListPane;

    public Config() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(gridBagLayout);
        this.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        rulePane = new RulePane();
        blackListPane = new BlackListPane();

//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.weightx = 1.0;
//        gbc.weighty = 0.0;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.anchor = GridBagConstraints.NORTHWEST;
//        this.add(new BlackListPane2(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(30, 0, 30, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(new JSeparator(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
//        blackListPane2.setBorder(new TitledBorder("list"));
        this.add(blackListPane, gbc);
    }

}

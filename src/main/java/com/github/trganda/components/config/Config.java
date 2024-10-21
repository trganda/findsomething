package com.github.trganda.components.config;

import javax.swing.*;
import java.awt.*;

public class Config extends JPanel {

    private RulePane rulePane;

    private BlackListPane2 blackListPane;

    public Config() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 20));

        rulePane = new RulePane();
        rulePane.setAlignmentX(Component.LEFT_ALIGNMENT);
        blackListPane = new BlackListPane2();
        blackListPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.add(rulePane);
        this.add(new JSeparator());
        this.add(blackListPane);
    }

}

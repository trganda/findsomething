package com.github.trganda.components.config;

import javax.swing.*;
import java.awt.*;

public class Config extends JPanel {

    private RulePane rulePane;

    private BlackListPane2 blackListPane2;

    public Config() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 20));

//        rulePane = new RulePane();
//        rulePane.setAlignmentX(Component.LEFT_ALIGNMENT);
        blackListPane2 = new BlackListPane2();
//        this.add(rulePane);
//        this.add(new JSeparator());
        this.add(blackListPane2);
    }

}

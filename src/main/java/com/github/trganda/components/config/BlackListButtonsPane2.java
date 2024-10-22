package com.github.trganda.components.config;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BlackListButtonsPane2 extends JPanel {

    private JComboBox<String> type;
    private JButton add;
    private JButton remove;
    private JButton clear;

    public BlackListButtonsPane2() {

        type = new JComboBox<>(new String[]{
           "Type 1", "Type 2", "Type 3", "Type 4"
        });
        add = new JButton("Add");
        remove = new JButton("Remove");
        clear = new JButton("Clear");

        setAlign(add, remove, clear);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(type);
        this.add(Box.createVerticalStrut(5));
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

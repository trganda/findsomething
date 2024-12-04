package com.github.trganda;

import java.awt.*;
import javax.swing.*;

public class AutoSizeJDialogExample {
  public static void main(String[] args) {
    // 创建主窗口
    JFrame frame = new JFrame("Main Window");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 300);

    // 按钮触发对话框
    JButton button = new JButton("Open Dialog");
    button.addActionListener(
        e -> {
          // 创建对话框
          JDialog dialog = new JDialog(frame, "Auto-sized Dialog", true);
          dialog.setLayout(new BorderLayout());

          // 添加组件
          JPanel panel = new JPanel();
          panel.add(new JLabel("This is a label."));
          panel.add(new JButton("Click Me"));
          dialog.add(panel, BorderLayout.CENTER);

          // 自动调整对话框大小
          dialog.pack();
          dialog.setLocationRelativeTo(frame);
          dialog.setVisible(true);
        });

    frame.add(button, BorderLayout.CENTER);
    frame.setVisible(true);
  }
}

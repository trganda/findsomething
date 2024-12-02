import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatCheckBoxMenuItem;
import com.formdev.flatlaf.extras.components.FlatPopupMenu;

import java.awt.*;
import javax.swing.*;

public class DynamicIconButton {
    // main function
    public static void main(String[] args) {
        // 设置 FlatLaf 样式
        FlatLightLaf.setup();

        // 创建主窗口
        JFrame frame = new JFrame("FlatCheckBoxMenuItem Custom Style Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(null);

        // 创建右键菜单
        FlatPopupMenu popupMenu = new FlatPopupMenu();

        // 创建带勾选框的菜单项
        FlatCheckBoxMenuItem checkBoxItem1 = new FlatCheckBoxMenuItem();
        checkBoxItem1.setSelected(true);
        checkBoxItem1.putClientProperty("JComponent.roundRect", true); // 设置圆角
//        checkBoxItem1.putClientProperty("JComponent.outline", "error"); // 设置错误样式的描边

        FlatCheckBoxMenuItem checkBoxItem2 = new FlatCheckBoxMenuItem();
        checkBoxItem1.setSelected(true);
        checkBoxItem2.putClientProperty("JComponent.roundRect", false); // 关闭圆角

        // 添加菜单项
        popupMenu.add(checkBoxItem1);
        popupMenu.add(checkBoxItem2);

        // 创建按钮并弹出菜单
        JButton button = new JButton("弹出菜单");
        button.setBounds(100, 70, 120, 30);
        button.addActionListener(e -> popupMenu.show(button, 0, button.getHeight()));
        frame.add(button);

        // 显示窗口
        frame.setVisible(true);
    }
}

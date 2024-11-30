import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class DynamicIconButton {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dynamic SVG Icon Button");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 创建按钮
            JButton button = new JButton("Dynamic Icon");
            button.setPreferredSize(new Dimension(200, 100));

            // 设置全局字体大小
            setGlobalFontSize(16);

            // 加载默认图标
            updateButtonIcon(button, 16);

            // 监听按钮大小变化
            button.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    float fontSize = button.getFont().getSize2D();
                    updateButtonIcon(button, fontSize);
                }
            });

            frame.add(button);
            frame.pack();
            frame.setVisible(true);
        });
    }

    // 更新按钮图标
    private static void updateButtonIcon(JButton button, float fontSize) {
        try {
            // 动态调整图标大小
            float iconSize = fontSize * 2.0f; // 图标尺寸为字体大小的 2 倍
            InputStream svgStream = DynamicIconButton.class.getResourceAsStream("filter.svg");
            if (svgStream != null) {
                BufferedImage iconImage = SVGUtils.renderSVG(svgStream, iconSize, iconSize);
                button.setIcon(new ImageIcon(iconImage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 设置全局字体大小
    private static void setGlobalFontSize(int size) {
        UIManager.getLookAndFeelDefaults().keys().asIterator().forEachRemaining(key -> {
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, ((Font) value).deriveFont((float) size));
            }
        });
    }
}
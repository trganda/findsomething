package com.github.trganda.components.dashboard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.ImageTranscoder;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOMetadata;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ButtonUI;

import com.github.trganda.utils.Utils;
import lombok.Getter;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.transcoder.*;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;

import static org.apache.batik.anim.dom.SVGDOMImplementation.SVG_NAMESPACE_URI;
import static org.apache.batik.transcoder.XMLAbstractTranscoder.*;

@Getter
public class FilterPane extends JPanel {
  private HostFilterPane hostFilter;
  private InformationFilterPane informationFilter;
  private JButton filterButton;

  public FilterPane() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupComponents(){
    URL filterURL = this.getClass().getClassLoader().getResource("svg/filter.svg");

//    ImageIcon icon = new ImageIcon(filterURL);
//    icon.setImage(icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));

    filterButton = new JButton();
    filterButton.setMargin(new Insets(0, 0, 0, 0));
    int fontSize = UIManager.getFont("Button.font").getSize();
    filterButton.setHorizontalAlignment(SwingConstants.LEFT);
    filterButton.setHorizontalTextPosition(SwingConstants.RIGHT);
    filterButton.setBackground((Color) UIManager.get("Button.hoverBackground"));
    // filterButton.setFocusPainted(false); // 移除焦点时的边框
    // Insets insets = filterButton.getBorder().getBorderInsets(filterButton);
    // filterButton.setBorder(new EmptyBorder(insets));
    // filterButton.setBorderPainted(false); // 不绘制边框
    filterButton.getModel().addChangeListener(e -> {
            ButtonModel model = filterButton.getModel();
            if (model.isPressed()) {
                model.setPressed(false); // 禁用悬停状态
            }
        });
    filterButton.putClientProperty("JButton.borderless", true);
    filterButton.setLayout(new FlowLayout(FlowLayout.LEFT));


//    // 使用 Apache Batik 加载 SVG 图标
    JSVGCanvas svgCanvas = new JSVGCanvas();
    svgCanvas.setURI(filterURL.toString());
    svgCanvas.setBackground((Color) UIManager.get("Button.hoverBackground"));
//    svgCanvas.setSize(new Dimension(24, 24));
//
//      // 将 SVG Canvas 嵌入到按钮中
    JPanel svgPanel = new JPanel(new BorderLayout());
    svgPanel.setBorder(null);
//    svgPanel.setBackground((Color) UIManager.get("Button.hoverBackground"));
    svgPanel.setPreferredSize(new Dimension(24, 24));
    svgPanel.add(svgCanvas, BorderLayout.CENTER);

//    // 添加图标和描述文本
    JLabel textLabel = new JLabel("Filter");
    filterButton.add(svgPanel); // 添加 SVG 图标
    filterButton.add(textLabel); // 添加文本描述


    // filterButton.setContentAreaFilled(false); // 移除背景填充
    // ButtonUI buttonUI = filterButton.getUI();
    // filterButton.getColorModel();
    // hostFilter = new HostFilterPane();
    // informationFilter = new InformationFilterPane();
  }

  private void setupLayout() {
    this.setLayout(new BorderLayout());
    this.add(filterButton, BorderLayout.CENTER);
    // GridBagLayout layout = new GridBagLayout();
    // GridBagConstraints gbc = new GridBagConstraints();

    // this.setLayout(layout);

    // gbc.gridx = 0;
    // gbc.gridy = 0;
    // gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    // gbc.insets = new Insets(0, 0, 0, 5);
    // this.add(hostFilter, gbc);

    // gbc.gridx = 1;
    // gbc.gridy = 0;
    // gbc.insets = new Insets(0, 0, 0, 0);
    // this.add(informationFilter, gbc);
  }

}

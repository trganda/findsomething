package com.github.trganda.components.dashboard;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import lombok.Getter;

@Getter
public class Dashboard extends JPanel {
  private InformationPane informationPane;
  private RequestSplitFrame requestSplitFrame;
  private JSplitPane dashSplitPane;
  private StatusPane statusPane;
  private HostFilterPane hostFilterPane;

  public Dashboard() {
    hostFilterPane = new HostFilterPane();
    informationPane = new InformationPane();
    requestSplitFrame = new RequestSplitFrame();
    statusPane = new StatusPane();

    dashSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    dashSplitPane.setLeftComponent(informationPane);
    dashSplitPane.setRightComponent(requestSplitFrame);
    dashSplitPane.setUI(
        new BasicSplitPaneUI() {

          @Override
          public BasicSplitPaneDivider createDefaultDivider() {
            return new BasicSplitPaneDivider(this) {
              @Override
              public void paint(Graphics g) {
                // hidden the default divider
                super.paint(g);
              }
            };
          }
        });

    this.setLayout(new GridBagLayout());
    this.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));
    GridBagConstraints gbc = new GridBagConstraints();

    // gbc.gridx = 0;
    // gbc.gridy = 0;
    // gbc.gridwidth = 2;
    // gbc.anchor = GridBagConstraints.CENTER;
    // gbc.insets = new Insets(0, 0, 5, 0);
    // this.add(hostFilterPane, gbc);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(0, 0, 5, 0);
    this.add(dashSplitPane, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(statusPane, gbc);
  }
}

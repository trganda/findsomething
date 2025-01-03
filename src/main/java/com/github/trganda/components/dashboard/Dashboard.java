package com.github.trganda.components.dashboard;

import com.github.trganda.components.common.InvisibleSplitPane;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class Dashboard extends JPanel {
  private InformationPanel informationPanel;
  private RequestSplitFrame requestSplitFrame;
  private InvisibleSplitPane dashSplitPane;
  private StatusPanel statusPanel;

  public Dashboard() {
    informationPanel = new InformationPanel();
    requestSplitFrame = new RequestSplitFrame();
    statusPanel = new StatusPanel();

    dashSplitPane = new InvisibleSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    dashSplitPane.setLeftComponent(informationPanel);
    dashSplitPane.setRightComponent(requestSplitFrame);

    this.setLayout(new GridBagLayout());
    this.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));
    GridBagConstraints gbc = new GridBagConstraints();

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
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(statusPanel, gbc);
  }
}

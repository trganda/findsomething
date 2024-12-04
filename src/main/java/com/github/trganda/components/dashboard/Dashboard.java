package com.github.trganda.components.dashboard;

import com.github.trganda.components.common.InvisibleSplitPane;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class Dashboard extends JPanel {
  private InformationPane informationPane;
  private RequestSplitFrame requestSplitFrame;
  private InvisibleSplitPane dashSplitPane;
  private StatusPane statusPane;

  public Dashboard() {
    informationPane = new InformationPane();
    requestSplitFrame = new RequestSplitFrame();
    statusPane = new StatusPane();

    dashSplitPane = new InvisibleSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    dashSplitPane.setLeftComponent(informationPane);
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
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(statusPane, gbc);
  }
}

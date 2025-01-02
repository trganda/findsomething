package com.github.trganda.components.dashboard;

import com.github.trganda.components.common.InvisibleSplitPane;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class RequestSplitFrame extends JPanel {

  private final InformationDetailsPanel informationDetailsPanel;
  private final RequestPanel requestPanel;
  private InvisibleSplitPane dashSplitPane;

  public RequestSplitFrame() {
    requestPanel = new RequestPanel();
    informationDetailsPanel = new InformationDetailsPanel();

    dashSplitPane = new InvisibleSplitPane(JSplitPane.VERTICAL_SPLIT);
    dashSplitPane.setTopComponent(informationDetailsPanel);
    dashSplitPane.setBottomComponent(requestPanel);
    this.setLayout(new BorderLayout());
    this.add(dashSplitPane);
  }
}

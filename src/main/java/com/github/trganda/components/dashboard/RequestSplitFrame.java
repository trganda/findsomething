package com.github.trganda.components.dashboard;

import com.github.trganda.components.common.InvisibleSplitPane;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class RequestSplitFrame extends JPanel {

  private final InformationDetailsPane informationDetailsPane;
  private final RequestPane requestPane;
  private InvisibleSplitPane dashSplitPane;

  public RequestSplitFrame() {
    requestPane = new RequestPane();
    informationDetailsPane = new InformationDetailsPane();

    dashSplitPane = new InvisibleSplitPane(JSplitPane.VERTICAL_SPLIT);
    dashSplitPane.setTopComponent(informationDetailsPane);
    dashSplitPane.setBottomComponent(requestPane);
    this.setLayout(new BorderLayout());
    this.add(dashSplitPane);
  }
}

package com.github.trganda.components.dashboard;

import javax.swing.*;
import lombok.Getter;

@Getter
public class RequestSplitFrame extends JSplitPane {

  private final InformationDetailsPane informationDetailsPane;
  private final RequestPane requestPane;

  public RequestSplitFrame() {
    requestPane = new RequestPane();
    informationDetailsPane = new InformationDetailsPane();
    this.setOrientation(VERTICAL_SPLIT);
    this.setTopComponent(informationDetailsPane);
    this.setBottomComponent(requestPane);
  }
}

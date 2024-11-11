package com.github.trganda.components.dashboard;

import java.awt.Graphics;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
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
    this.setContinuousLayout(true);
    this.setUI(
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
  }
}

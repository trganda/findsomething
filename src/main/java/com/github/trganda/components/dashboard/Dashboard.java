package com.github.trganda.components.dashboard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.github.trganda.FindSomething;
import com.github.trganda.components.InvisibleSplitPane;
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

//    UIManager.put("SplitPaneDivider.gripDotCount", 0);
//    UIManager.put("SplitPaneDivider.hoverColor", Color.WHITE);

    dashSplitPane = new InvisibleSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    dashSplitPane.setLeftComponent(informationPane);
    dashSplitPane.setRightComponent(requestSplitFrame);
//    BasicSplitPaneDivider divider = ((BasicSplitPaneUI)dashSplitPane.getUI()).getDivider();
//    divider.setVisible(false);
//    divider.setBorder(new EmptyBorder(0, 0, 0, 0));

//    dashSplitPane.addPropertyChangeListener(e -> {
////      FindSomething.API.logging().logToOutput(dashSplitPane.getUI().toString());
////      divider = ((BasicSplitPaneUI)dashSplitPane.getUI()).getDivider();
////      divider.setBorder(new EmptyBorder(0, 0, 0, 0));
//      divider.setVisible(false);
////      FindSomething.API.logging().logToOutput(divider.toString());
////      UIManager.put("SplitPaneDivider.gripDotCount", 0);
////      UIManager.put("SplitPaneDivider.hoverColor", Color.WHITE);
////      FindSomething.API.logging().logToOutput(dashSplitPane.getUI().toString());
////      if (dashSplitPane.getUI() instanceof CustomBasicSplitPaneUI) {
////        return;
////      }
////      dashSplitPane.setUI(
////              new CustomBasicSplitPaneUI());
////      dashSplitPane.revalidate();
//    });

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

  private class CustomBasicSplitPaneUI extends BasicSplitPaneUI {
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
  }
}

package com.github.trganda.components.dashboard;

import com.github.trganda.components.common.PlaceHolderTextField;
import com.github.trganda.components.renderer.LeftAlignTableCellRenderer;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.github.trganda.controller.dashboard.InfoInnerFilterController;
import lombok.Getter;

@Getter
public class InformationInnerPanel extends JPanel {
  private PlaceHolderTextField filterField;
  private JCheckBox sensitive;
  private JCheckBox negative;
  private JTable infoTable;
  private DefaultTableModel infoTableModel;
  private JScrollPane infoTableScrollPane;

  private JButton settingButton;
  private JPopupMenu settingMenu;

  public InformationInnerPanel() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupComponents() {
    filterField = new PlaceHolderTextField("Search");
    sensitive = new JCheckBox("Case sensitive");
    sensitive.setBorder(new EmptyBorder(5, 5, 5, 5));
    negative = new JCheckBox("Negative search");
    negative.setBorder(new EmptyBorder(0, 5, 5, 5));

    settingMenu = new JPopupMenu();
    settingMenu.add(sensitive);
    settingMenu.add(negative);

    infoTable = new JTable();
     infoTableModel =
        new DefaultTableModel(new Object[] {"Info"}, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
    infoTable.setModel(infoTableModel);
    TableCellRenderer headerRenderer = infoTable.getTableHeader().getDefaultRenderer();
    infoTable.getTableHeader().setDefaultRenderer(new LeftAlignTableCellRenderer(headerRenderer));
    infoTableScrollPane = new JScrollPane(infoTable);

    settingButton = new JButton("Settings");
    settingButton.addActionListener(
        e -> {
          int popupHeight = settingMenu.getPreferredSize().height;
          settingMenu.show(settingButton, 0, -popupHeight);
        });

    new InfoInnerFilterController(this);
  }

  private void setupLayout() {
    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weighty = 1;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(0, 0, 5, 0);
    gbc.anchor = GridBagConstraints.CENTER;
    this.add(infoTableScrollPane, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weighty = 0;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.insets = new Insets(0, 0, 0, 5);
    this.add(settingButton, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(filterField, gbc);
  }
}

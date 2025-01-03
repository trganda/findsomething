package com.github.trganda.controller.dashboard;

import com.github.trganda.components.common.PlaceHolderTextField;
import com.github.trganda.components.dashboard.InformationInnerPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class InfoInnerFilterController {

  private InformationInnerPanel infoInnerPanel;

  public InfoInnerFilterController(InformationInnerPanel infoInnerPanel) {
    this.infoInnerPanel = infoInnerPanel;
    this.setupEventListener();
  }

  private void setupEventListener() {
    // Set up a default table filter
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(infoInnerPanel.getInfoTableModel());
    infoInnerPanel.getInfoTable().setRowSorter(sorter);

    PlaceHolderTextField textField = this.infoInnerPanel.getFilterField();
    this.infoInnerPanel.getFilterField().getDocument().addDocumentListener(
            new DocumentListener() {
              @Override
              public void insertUpdate(DocumentEvent e) {
                if (textField.isPlaceholderActive()) {
                  return;
                }
                updateTableFilter(
                        textField.getText(),
                        infoInnerPanel.getSensitive().isSelected(),
                        infoInnerPanel.getNegative().isSelected());
              }

              @Override
              public void removeUpdate(DocumentEvent e) {
                if (textField.isPlaceholderActive()) {
                  return;
                }
                updateTableFilter(
                        textField.getText(),
                        infoInnerPanel.getSensitive().isSelected(),
                        infoInnerPanel.getNegative().isSelected());
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
                if (textField.isPlaceholderActive()) {
                  return;
                }
                updateTableFilter(
                        textField.getText(),
                        infoInnerPanel.getSensitive().isSelected(),
                        infoInnerPanel.getNegative().isSelected());
              }
            }
    );
  }

  /**
   * Updates the table filter.
   * <p>
   * The filter string is interpreted as a regular expression. If the filter string is empty,
   * the filter is disabled. If the filter string is not empty but does not parse as a regular
   * expression, the filter is not changed. If the filter string does parse as a regular
   * expression, the filter is updated. If the negative flag is set, the filter is inverted.
   * <p>
   * The filter is applied to all tabs in the {@code infoPane} component.
   *
   * @param filter the filter string
   * @param sensitive whether the filter is case-sensitive
   * @param negative whether the filter is inverted
   */
  public void updateTableFilter(String filter, boolean sensitive, boolean negative) {
    RowFilter<TableModel, Object> rf = null;
    // if current expression doesn't parse, don't update.
    try {
      if (!sensitive) {
        filter = "(?i)" + filter;
      }

      if (negative) {
        // filter the first column
        rf = RowFilter.notFilter(RowFilter.regexFilter(filter, 0));
      } else {
        rf = RowFilter.regexFilter(filter, 0);
      }

    } catch (java.util.regex.PatternSyntaxException e) {
      return;
    }

    JTable table = this.infoInnerPanel.getInfoTable();
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
    sorter.setRowFilter(rf);
    table.setRowSorter(sorter);
  }
}

package com.github.trganda.components.config;

import static com.github.trganda.config.ConfigManager.*;

import com.github.trganda.components.common.PlaceHolderTextField;
import com.github.trganda.components.renderer.LeftAlignTableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import lombok.Getter;

@Getter
public class RuleInnerPane extends JPanel {

  private JComboBox<String> selector;
  private JLabel countLabel;

  private PlaceHolderTextField ruleSearch;
  private RuleInnerButtonsPane ruleButtonsPane;
  private JTable table;
  private DefaultTableModel model;
  private JComponent wrap;

  public RuleInnerPane() {
    this.setupComponents();
    this.setupLayout();
  }

  private void setupLayout() {
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    this.add(new JLabel("Rule group:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(selector, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(5, 0, 0, 0);
    this.add(new JLabel("Rule count:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    this.add(countLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(15, 0, 20, 0);
    this.add(new JSeparator(), gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.insets = new Insets(0, 0, 5, 0);
    this.add(ruleSearch, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.insets = new Insets(0, 0, 5, 5);
    this.add(ruleButtonsPane, gbc);

    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(wrap, gbc);
  }

  private JComponent setupTable() {
    model =
        new DefaultTableModel(
            new Object[] {"Enabled", "Name", "Regex", "Capturing Group", "Scope", "Sensitive"}, 0) {
          @Override
          public Class<?> getColumnClass(int column) {
            // set column to using combobox
            return (column == 0) ? Boolean.class : String.class;
          }

          @Override
          public boolean isCellEditable(int row, int column) {
            return column == 0;
          }
        };
    table = new JTable(model);
    TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
    table.getTableHeader().setDefaultRenderer(new LeftAlignTableCellRenderer(headerRenderer));

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setPreferredSize(new Dimension(table.getPreferredSize().width, 160));
    scrollPane.addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            resizePane();
          }
        });

    return scrollPane;
  }

  private void resizePane() {
    int width = table.getWidth();
    table.getColumnModel().getColumn(0).setPreferredWidth((int) (width * 0.1));
    table.getColumnModel().getColumn(1).setPreferredWidth((int) (width * 0.2));
    table.getColumnModel().getColumn(2).setPreferredWidth((int) (width * 0.5));
    table.getColumnModel().getColumn(3).setPreferredWidth((int) (width * 0.1));
    table.getColumnModel().getColumn(4).setPreferredWidth((int) (width * 0.1));
    table.getColumnModel().getColumn(5).setPreferredWidth((int) (width * 0.1));
  }

  private void setupComponents() {
    ruleButtonsPane = new RuleInnerButtonsPane();
    selector =
        new JComboBox<>(
            new String[] {
              GROUP_FINGERPRINT, GROUP_SENSITIVE, GROUP_VULNERABILITY, GROUP_INFORMATION
            });

    ruleSearch = new PlaceHolderTextField("Search");
    ruleSearch.setPreferredSize(new Dimension(260, ruleSearch.getPreferredSize().height));
    wrap = this.setupTable();
    countLabel = new JLabel("0");
  }
}

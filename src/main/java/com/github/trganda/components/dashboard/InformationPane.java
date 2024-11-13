package com.github.trganda.components.dashboard;

import static com.github.trganda.config.Config.GROUP_FINGERPRINT;
import static com.github.trganda.config.Config.GROUP_GENERAL;
import static com.github.trganda.config.Config.GROUP_INFORMATION;
import static com.github.trganda.config.Config.GROUP_SENSITIVE;
import static com.github.trganda.config.Config.GROUP_VULNERABILITY;

import com.github.trganda.components.UIRender;
import com.github.trganda.components.renderer.LeftAlignTableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import lombok.Getter;

@Getter
public class InformationPane extends JPanel {
  private final String filterPlaceHolder = "Search";
  private JTable infoTable;
  private DefaultTableModel infoTableModel;
  private TableRowSorter<DefaultTableModel> sorter;
  private JComponent wrap;
  private JComboBox<String> selector;
  private JTextField filterField;

  public InformationPane() {
    this.setMinimumSize(new Dimension(420, this.getPreferredSize().height));

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
    gbc.insets = new Insets(0, 0, 5, 5);
    this.add(new JLabel("Group:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 5, 0);
    this.add(selector, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(0, 0, 5, 0);
    this.add(wrap, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(filterField, gbc);
  }

  private void setupComponents() {
    wrap = setupTable();
    selector =
        new JComboBox<>(
            new String[] {
              GROUP_GENERAL, GROUP_FINGERPRINT, GROUP_SENSITIVE, GROUP_VULNERABILITY, GROUP_INFORMATION
            });

    filterField = new JTextField(filterPlaceHolder);
    filterField = UIRender.renderTextField(filterField, filterPlaceHolder);
  }

  private JComponent setupTable() {
    infoTable = new JTable();
    infoTableModel =
        new DefaultTableModel(new Object[] {"#", "Info"}, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
    infoTable.setModel(infoTableModel);
    TableCellRenderer headerRenderer = infoTable.getTableHeader().getDefaultRenderer();
    infoTable.getTableHeader().setDefaultRenderer(new LeftAlignTableCellRenderer(headerRenderer));

    // sorter and filter
    sorter = new TableRowSorter<DefaultTableModel>(infoTableModel);
    infoTable.setRowSorter(sorter);

    JScrollPane infoTableScrollPane = new JScrollPane(infoTable);
    infoTableScrollPane.addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            resizePane();
          }
        });

    return infoTableScrollPane;
  }

  private void resizePane() {
    int infoTableWidth = infoTable.getWidth();
    infoTable.getColumnModel().getColumn(0).setPreferredWidth((int) (infoTableWidth * 0.1));
    infoTable.getColumnModel().getColumn(1).setPreferredWidth((int) (infoTableWidth * 0.9));
  }
}

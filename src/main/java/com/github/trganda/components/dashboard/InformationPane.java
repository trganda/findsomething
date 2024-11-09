package com.github.trganda.components.dashboard;

import static com.github.trganda.config.Config.GROUP_FINGERPRINT;
import static com.github.trganda.config.Config.GROUP_INFORMATION;
import static com.github.trganda.config.Config.GROUP_SENSITIVE;
import static com.github.trganda.config.Config.GROUP_VULNERABILITY;

import com.github.trganda.handler.DataChangeListener;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.utils.Utils;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class InformationPane extends JPanel implements DataChangeListener {
  private final String placeHolder = "Search";
  private JTable infoTable;
  private DefaultTableModel infoTableModel;
  private TableRowSorter<DefaultTableModel> sorter;
  private JComponent wrap;
  private JComboBox<String> selector;
  private JTextField filterField;

  public InformationPane() {
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
    gbc.insets = new Insets(0, 0, 5, 10);
    JLabel label = new JLabel("Type:");
    this.add(label, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 5, 0);
    this.add(selector, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.insets = new Insets(0, 0, 5, 10);
    JLabel filterLabel = new JLabel("Filter:");
    this.add(filterLabel, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 5, 0);
    this.add(filterField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(wrap, gbc);
  }

  private void setupComponents() {
    wrap = setupTable();
    selector =
        new JComboBox<>(
            new String[] {
              GROUP_FINGERPRINT, GROUP_SENSITIVE, GROUP_VULNERABILITY, GROUP_INFORMATION
            });

    // selector.addActionListener(
    //     e -> {
    //       String group = selector.getSelectedItem().toString();
    //       if (group == null) {
    //         return;
    //       }

    //       List<InfoDataModel> data = CachePool.getInstance().getInfoData(group);
    //       if (data != null) {
    //         this.loadInfoWithGroup(data);
    //       }
    //     });
    filterField = new JTextField(placeHolder);
    filterField.setFont(
        new Font(
            Utils.getBurpDisplayFont().getName(),
            Font.PLAIN,
            Utils.getBurpDisplayFont().getSize()));
    filterField.setForeground(Color.GRAY);
    // filterField
    //     .getDocument()
    //     .addDocumentListener(
    //         new DocumentListener() {
    //           public void changedUpdate(DocumentEvent e) {
    //             newFilter();
    //           }

    //           public void insertUpdate(DocumentEvent e) {
    //             newFilter();
    //           }

    //           public void removeUpdate(DocumentEvent e) {
    //             newFilter();
    //           }
    //         });
    filterField.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent e) {
            super.focusGained(e);
            if (filterField.getText().equals(placeHolder)) {
              filterField.setFont(
                  new Font(
                      Utils.getBurpDisplayFont().getName(),
                      Font.PLAIN,
                      Utils.getBurpDisplayFont().getSize()));
              filterField.setForeground(Color.BLACK);
              filterField.setText("");
            }
          }

          @Override
          public void focusLost(FocusEvent e) {
            super.focusLost(e);
            if (filterField.getText().isEmpty()) {
              filterField.setFont(
                  new Font(
                      Utils.getBurpDisplayFont().getName(),
                      Font.ITALIC,
                      Utils.getBurpDisplayFont().getSize()));
              filterField.setForeground(Color.GRAY);
              filterField.setText(placeHolder);
            }
          }
        });
  }

  // private void newFilter() {
  //   RowFilter<DefaultTableModel, Object> rf = null;
  //   // If current expression doesn't parse, don't update.
  //   try {
  //     rf = RowFilter.regexFilter(filterField.getText(), 1);
  //   } catch (java.util.regex.PatternSyntaxException e) {
  //     return;
  //   }
  //   sorter.setRowFilter(rf);
  // }

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

  // private void loadInfoWithGroup(List<InfoDataModel> data) {
  //   SwingWorker<List<Object[]>, Void> worker =
  //       new SwingWorker<>() {

  //         @Override
  //         protected List<Object[]> doInBackground() throws Exception {
  //           List<Object[]> infos = new ArrayList<>();
  //           for (InfoDataModel row : data) {
  //             infos.add(row.getInfoData());
  //           }
  //           return infos;
  //         }

  //         @Override
  //         protected void done() {
  //           // update when work done
  //           try {
  //             infoTableModel.setRowCount(0);
  //             List<Object[]> rows = get();
  //             for (Object[] row : rows) {
  //               infoTableModel.addRow(row);
  //             }
  //             infoTableModel.fireTableDataChanged();
  //           } catch (InterruptedException | ExecutionException e) {
  //             FindSomething.API.logging().logToError(new RuntimeException(e));
  //           }
  //         }
  //         ;
  //       };

  //   worker.execute();
  // }

  private void resizePane() {
    int infoTableWidth = infoTable.getWidth();
    infoTable.getColumnModel().getColumn(0).setPreferredWidth((int) (infoTableWidth * 0.1));
    infoTable.getColumnModel().getColumn(1).setPreferredWidth((int) (infoTableWidth * 0.9));
  }

  @Override
  public void onDataChanged(List<InfoDataModel> data) {
    // String group = selector.getSelectedItem().toString();
    // List<InfoDataModel> d = CachePool.getInstance().getInfoData(group);

    // if (d != null) {
    //   this.loadInfoWithGroup(d);
    // }
  }

  // public JTable getInfoTable() {
  //   return infoTable;
  // }
}

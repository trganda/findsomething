package com.github.trganda.components.dashboard;

import com.github.trganda.FindSomething;
import com.github.trganda.config.Config;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.handler.DataChangeListener;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.cache.CachePool;

import static com.github.trganda.config.Config.GROUP_FINGERPRINT;
import static com.github.trganda.config.Config.GROUP_INFORMATION;
import static com.github.trganda.config.Config.GROUP_SENSITIVE;
import static com.github.trganda.config.Config.GROUP_VULNERABILITY;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InformationPane extends JPanel implements DataChangeListener {
  private JTable infoTable;
  private DefaultTableModel infoTableModel;
  private JComponent wrap;
  private JComboBox<String> selector;

  public InformationPane() {
    this.setupComponents();

    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.insets = new Insets(0, 0, 10, 10);
    JLabel label = new JLabel("Type:");
    this.add(label, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 10, 0);
    this.add(selector, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1.0;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(0, 0, 0, 0);
    this.add(wrap, gbc);
  }

  private void setupComponents() {

    wrap = setupTable();
    selector = new JComboBox<>(
      new String[] {
        GROUP_FINGERPRINT, GROUP_SENSITIVE, GROUP_VULNERABILITY, GROUP_INFORMATION
      });
    
    selector.addActionListener(
      e -> {
        String group = selector.getSelectedItem().toString();
        if (group == null) {
          return;
        }

        List<InfoDataModel> data = CachePool.getInstance().getInfoData(group);
        if (data != null) {
          this.loadInfoWithGroup(data);  
        }
      });
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

  private void loadInfoWithGroup(List<InfoDataModel> data) {
    SwingWorker<List<Object[]>, Void> worker = 
      new SwingWorker<>() {
      
        @Override
        protected List<Object[]> doInBackground() throws Exception {
            // TODO Auto-generated method stub
            List<Object[]> infos = new ArrayList<>();
            for (InfoDataModel row : data) {
              infos.add(row.getInfoData());
            }
            return infos;
        }

        @Override
        protected void done() {
          // update when work done
          try {
            infoTableModel.setRowCount(0);
            List<Object[]> rows = get();
            for (Object[] row : rows) {
              infoTableModel.addRow(row);
            }
            infoTableModel.fireTableDataChanged();
          } catch (InterruptedException | ExecutionException e) {
            FindSomething.API.logging().logToError(new RuntimeException(e));
          }
        };
      };
    
    worker.execute();
  }

  private void resizePane() {
    int infoTableWidth = infoTable.getWidth();
    infoTable.getColumnModel().getColumn(0).setPreferredWidth((int) (infoTableWidth * 0.1));
    infoTable.getColumnModel().getColumn(1).setPreferredWidth((int) (infoTableWidth * 0.9));
  }

  @Override
  public void onDataChanged(List<InfoDataModel> data) {
    String group = selector.getSelectedItem().toString();
    List<InfoDataModel> d = CachePool.getInstance().getInfoData(group);
    FindSomething.API.logging().logToOutput(group + " changed" + " size: " + d.size());
    if (d != null) {
      this.loadInfoWithGroup(d);  
    }
    // SwingWorker<List<Object[]>, Void> worker =
    //     new SwingWorker<>() {
    //       @Override
    //       protected List<Object[]> doInBackground() {
    //         ArrayList<Object[]> rdata = new ArrayList<>();
    //         for (InfoDataModel row : data) {
    //           rdata.add(row.getInfoData());
    //         }
    //         return rdata;
    //       }

    //       @Override
    //       protected void done() {
    //         // update when work done
    //         try {
    //           List<Object[]> rows = get();
    //           for (Object[] row : rows) {
    //             infoTableModel.addRow(row);
    //           }
    //           infoTableModel.fireTableDataChanged();
    //         } catch (InterruptedException | ExecutionException e) {
    //           FindSomething.API.logging().logToError(new RuntimeException(e));
    //         }
    //       }
    //     };
    // worker.execute();
  }

  public JTable getInfoTable() {
    return infoTable;
  }
}

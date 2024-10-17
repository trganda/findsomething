package com.github.trganda.componets;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.trganda.componets.pane.FilterSplitPane;
import com.github.trganda.componets.pane.RequestPane;
import com.github.trganda.model.cache.CachePool;
import com.github.trganda.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPane extends JPanel {

    private final MontoyaApi api;
    private final FilterSplitPane filterSplitPane;
    private final RequestPane requestPane;

    public MainPane(MontoyaApi api) {
        this.api = api;
        this.setLayout(new BorderLayout());

        JSplitPane dashboard = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        filterSplitPane = new FilterSplitPane(api);
        requestPane = new RequestPane(api);

        dashboard.setTopComponent(filterSplitPane);
        dashboard.setBottomComponent(requestPane);

        // main panel
        JTabbedPane mainTabs = new JTabbedPane();
        mainTabs.addTab("Dashboard", dashboard);
        mainTabs.addTab("Config", new ConfigPane());

        this.add(mainTabs);

        // setup event
        setupTable();
    }

    public FilterSplitPane getFilterSplitPane() {
        return filterSplitPane;
    }

    private void setupTable() {
        filterSplitPane.getReqInfoTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                api.logging().logToOutput("mouseClicked");
                int row = filterSplitPane.getReqInfoTable().rowAtPoint(e.getPoint());
                if (row >= 0) {
                    String path = (String) filterSplitPane.getReqInfoTable().getValueAt(row, 1);
                    String host = (String) filterSplitPane.getReqInfoTable().getValueAt(row, 2);
                    String hash = Utils.calHash(path, host);
                    InterceptedResponse resp =  CachePool.getInterceptedResponse(hash);

                    requestPane.getRequestEditor().setRequest(resp.request());
                    requestPane.getResponseEditor().setResponse(resp);
                }
            }
        });
    }
}

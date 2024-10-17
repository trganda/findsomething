package com.github.trganda.componets.pane;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;

import javax.swing.*;

public class RequestPane extends JTabbedPane {

    private final HttpRequestEditor requestEditor;
    private final HttpResponseEditor responseEditor;
    private final MontoyaApi api;

    public RequestPane(MontoyaApi api) {
        this.api = api;
        requestEditor = api.userInterface().createHttpRequestEditor();
        responseEditor = api.userInterface().createHttpResponseEditor();
        this.addTab("Request", requestEditor.uiComponent());
        this.addTab("Response", responseEditor.uiComponent());
    }

    public HttpRequestEditor getRequestEditor() {
        return requestEditor;
    }

    public HttpResponseEditor getResponseEditor() {
        return responseEditor;
    }
}

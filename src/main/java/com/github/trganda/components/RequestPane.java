package com.github.trganda.components;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import com.github.trganda.FindSomething;

import javax.swing.*;

public class RequestPane extends JTabbedPane {

    private final HttpRequestEditor requestEditor;
    private final HttpResponseEditor responseEditor;

    public RequestPane() {
        requestEditor = FindSomething.api.userInterface().createHttpRequestEditor();
        responseEditor = FindSomething.api.userInterface().createHttpResponseEditor();
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

package com.jmd.browser.inst.base;

import java.awt.Component;

import com.jmd.callback.CommonAsyncCallback;
import com.jmd.callback.JavaScriptExecutionCallback;

public interface AbstractBrowser {

    void create(String url, CommonAsyncCallback callback);

    Object getBrowser();

    Component getBrowserContainer();

    Object getDevTools();

    Component getDevToolsContainer();

    String getVersion();

    public void reload();

    public void loadURL(String url);

    public void dispose(int a);

    public void clearLocalStorage();

    public void execJS(String javaScript);

    public void execJSWithStringBack(String javaScript, JavaScriptExecutionCallback callback);

}

package com.jmd.browser.inst;

import java.awt.Component;

import com.jetbrains.cef.JCefAppConfig;
import com.jmd.ApplicationConfig;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefRendering;

import com.jmd.browser.inst.base.AbstractBrowser;
import com.jmd.callback.CommonAsyncCallback;
import com.jmd.callback.JavaScriptExecutionCallback;

public class ChromiumEmbeddedCoreInst implements AbstractBrowser {

    private static volatile ChromiumEmbeddedCoreInst instance;

    private boolean isCreated = false;
    private CefApp cefApp = null;
    private CefClient cefClient = null;
    private CefBrowser browser = null;

    @Override
    public void create(String url, CommonAsyncCallback callback) {
        if (!isCreated) {
            String[] args = JCefAppConfig.getInstance().getAppArgs();
            CefSettings settings = JCefAppConfig.getInstance().getCefSettings();
            settings.cache_path = System.getProperty("user.dir") + "/context/jcef/data_" + ApplicationConfig.startPort;
            // 获取CefApp实例
            CefApp.startup(args);
            cefApp = CefApp.getInstance(args, settings);
            isCreated = true;
        }
        // 创建客户端实例
        cefClient = cefApp.createClient();
        // 创建浏览器实例
        browser = cefClient.createBrowser(url, CefRendering.DEFAULT, true);
        // 完成回调
        callback.execute();
    }

    @Override
    public Object getBrowser() {
        return this.browser;
    }

    @Override
    public Component getBrowserContainer() {
        return this.browser.getUIComponent();
    }

    @Override
    public Object getDevTools() {
        return this.browser.getDevTools();
    }

    @Override
    public Component getDevToolsContainer() {
        return this.browser.getDevTools().getUIComponent();
    }

    @Override
    public String getVersion() {
        return "Chromium Embedded Framework (CEF), " + "ChromeVersion: " + cefApp.getVersion().getChromeVersion();
    }

    @Override
    public void reload() {
        this.browser.reload();
    }

    @Override
    public void loadURL(String url) {
        this.browser.loadURL(url);
    }

    @Override
    public void dispose(int a) {
        if (a == 0) {
            cefApp.dispose();
        } else {
            cefClient.dispose();
        }
    }

    @Override
    public void clearLocalStorage() {
        this.execJS("localStorage.removeItem(\"jmd-config\")");
    }

    @Override
    public void execJS(String javaScript) {
        this.browser.executeJavaScript(javaScript, null, 0);
    }

    @Override
    public void execJSWithStringBack(String javaScript, JavaScriptExecutionCallback callback) {

    }

    public static ChromiumEmbeddedCoreInst getInstance() {
        if (instance == null) {
            synchronized (ChromiumEmbeddedCoreInst.class) {
                if (instance == null) {
                    instance = new ChromiumEmbeddedCoreInst();
                }
            }
        }
        return instance;
    }

}

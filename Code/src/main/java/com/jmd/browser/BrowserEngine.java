package com.jmd.browser;

import javax.swing.SwingWorker;

import com.jmd.ApplicationConfig;
import com.jmd.callback.BrowserInitCallback;
import com.jmd.web.common.WsSendData;
import com.jmd.web.websocket.handler.MapWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.Application;
import com.jmd.browser.inst.ChromiumEmbeddedCoreInst;
import com.jmd.browser.inst.base.AbstractBrowser;
import com.jmd.callback.JavaScriptExecutionCallback;

@Component
public class BrowserEngine {

    @Autowired
    private MapWebSocketHandler wsHandler;

    private BrowserType type = BrowserType.CHROMIUM_EMBEDDED_CEF_BROWSER;
    private AbstractBrowser browser = null;

    public void init(BrowserInitCallback callback) {
        switch (type) {
            case CHROMIUM_EMBEDDED_CEF_BROWSER -> browser = ChromiumEmbeddedCoreInst.getInstance();
            default -> {
            }
        }
        String url = "http://localhost:" + ApplicationConfig.startPort + "/web/index.html";
        // String url = "http://localhost:4500";
        // String url = "http://intelyes.club";
        browser.create(url,
                () -> new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        while (true) {
                            if (Application.isStartFinish()) {
                                callback.execute(browser);
                                break;
                            }
                            try {
                                Thread.sleep(150);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                }.execute());
    }

    public void changeCore(BrowserType changeType, BrowserInitCallback callback) {
        if (this.type == changeType) {
            return;
        }
        this.type = changeType;
        this.browser.dispose(1);
        this.init(callback);
    }

    public AbstractBrowser getBrowser() {
        return this.browser;
    }

    public void reload() {
        this.browser.reload();
    }

    public void dispose() {
        this.browser.dispose(0);
    }

    public void clearLocalStorage() {
        this.browser.clearLocalStorage();
    }

    public void sendMessageByWebsocket(String topic, String message) {
        this.wsHandler.send(new WsSendData(topic, message));
    }

    public void execJS(String javaScript) {
        this.browser.execJS(javaScript);
    }

    public BrowserEngine execJSWithStringBack(String javaScript, JavaScriptExecutionCallback callback) {
        this.browser.execJSWithStringBack(javaScript, callback);
        return this;
    }

    public BrowserType getBrowserType() {
        return this.type;
    }

    public String getVersion() {
        return this.browser.getVersion();
    }

}

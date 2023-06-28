package com.jmd.ui.tab.a_map.panel;

import java.io.Serial;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.ui.common.BrowserViewPanel;
import com.jmd.util.CommonUtils;
import com.jmd.web.common.WsSendData;
import com.jmd.web.websocket.handler.MapWebSocketHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MapControlBrowserPanel extends BrowserViewPanel {

    @Serial
    private static final long serialVersionUID = 5503359353536143127L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    @Value("${setting.web.prod}")
    private boolean prod;

    @Autowired
    private MapWebSocketHandler wsHandler;

    public MapControlBrowserPanel() {
        super("/map-control", "WebView初始化");
    }

    @PostConstruct
    private void init() {
        try {
            this.subInnerMqMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    protected void destroy() {
        this.innerMqService.destroyClient(this.client);
    }

    private void subInnerMqMessage() throws Exception {
        this.client = innerMqService.createClient();
        this.client.sub(Topic.APPLICATION_START_FINISH, (res) -> {
            if (this.isLoaded()) {
                this.reload();
            } else {
                this.load(this.prod);
            }
        });
        this.client.sub(Topic.OPEN_BROWSER_DEV_TOOL, (res) -> {
            this.toggleDevTools();
        });
    }

    public void clearLocalStorage() {
        this.execJS("localStorage.removeItem(\"jmd-config\")");
    }

    public void sendMessageByWebsocket(String topic, String message) {
        this.wsHandler.send(new WsSendData(topic, message));
    }

}
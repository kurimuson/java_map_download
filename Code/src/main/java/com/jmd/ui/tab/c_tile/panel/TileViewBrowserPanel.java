package com.jmd.ui.tab.c_tile.panel;

import com.jmd.model.tile.TileViewParam;
import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.ui.common.BrowserViewPanel;
import com.jmd.util.CommonUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serial;

@Component
public class TileViewBrowserPanel extends BrowserViewPanel {

    @Serial
    private static final long serialVersionUID = -6812120790251794674L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    @Value("${setting.web.prod}")
    private boolean prod;

    public TileViewBrowserPanel() {
        super("/tile-view", "请选择瓦片并加载");
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
        this.client.<TileViewParam>sub(Topic.OPEN_TILE_VIEW, (res) -> {
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

}

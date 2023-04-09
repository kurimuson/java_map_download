package com.jmd.ui.common;

import com.jmd.ApplicationStore;
import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.ui.frame.download.config.DownloadConfigFrame;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public abstract class CommonSubFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = -3945359489263563093L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    public CommonSubFrame() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(DownloadConfigFrame.class.getResource("/com/jmd/assets/icon/java.png")));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            this.subInnerMqMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void destroy() {
        this.innerMqService.destroyClient(this.client);
    }

    private void subInnerMqMessage() throws Exception {
        this.client = this.innerMqService.createClient();
        this.client.sub(Topic.UPDATE_UI, (res) -> {
            SwingUtilities.invokeLater(() -> {
                SwingUtilities.updateComponentTreeUI(this);
            });
        });
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            int x = ApplicationStore.MAIN_FRAME_LOCATION_X - (int) this.getSize().getWidth() / 2 + ApplicationStore.MAIN_FRAME_WIDTH / 2;
            int y = ApplicationStore.MAIN_FRAME_LOCATION_Y - (int) this.getSize().getHeight() / 2 + ApplicationStore.MAIN_FRAME_HEIGHT / 2;
            this.setLocation(x, y);
        }
        super.setVisible(b);
    }

}

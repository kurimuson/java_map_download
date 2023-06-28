package com.jmd.ui.tab.a_map.panel;

import com.jmd.common.StaticVar;
import com.jmd.http.ProxySetting;
import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.Serial;

@Component
public class StatusInfoPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 2553207209432671195L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    private final JLabel proxyStatusValue;

    public StatusInfoPanel() {

        JLabel proxyStatusLabel = new JLabel("代理状态：");
        proxyStatusLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        this.proxyStatusValue = new JLabel("");
        proxyStatusLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(proxyStatusLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(proxyStatusValue)
                        .addContainerGap(50, Short.MAX_VALUE)));
        groupLayout
                .setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup().addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(proxyStatusLabel).addComponent(proxyStatusValue))
                                .addContainerGap(0, Short.MAX_VALUE)));
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {
        this.updateProxyStatus();
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
        this.client = this.innerMqService.createClient();
        this.client.sub(Topic.UPDATE_PROXY_STATUS, (res) -> {
            this.updateProxyStatus();
        });
    }

    public void updateProxyStatus() {
        SwingUtilities.invokeLater(() -> {
            if (ProxySetting.enable) {
                proxyStatusValue.setText("开启");
                proxyStatusValue.setForeground(Color.GREEN);
            } else {
                proxyStatusValue.setText("关闭");
                proxyStatusValue.setForeground(Color.BLUE);
            }
        });
    }

}

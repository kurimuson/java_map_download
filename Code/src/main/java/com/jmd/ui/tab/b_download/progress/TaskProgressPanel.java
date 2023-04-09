package com.jmd.ui.tab.b_download.progress;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.Serial;
import javax.swing.GroupLayout.Alignment;

@Component
public class TaskProgressPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -4117937206677840485L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    private JProgressBar progressBar;

    @PostConstruct
    private void init() {

        this.progressBar = new JProgressBar();
        this.progressBar.setMinimum(0);
        this.progressBar.setMaximum(100);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(10)
                                .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addGap(10))
        );
        this.setLayout(groupLayout);

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
        this.client.<Integer>sub(Topic.DOWNLOAD_CONSOLE_PROGRESS, (res) -> {
            SwingUtilities.invokeLater(() -> progressBar.setValue(res));
        });
    }

}

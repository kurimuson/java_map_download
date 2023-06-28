package com.jmd.ui.tab.b_download.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.Serial;

import javax.swing.*;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.ui.tab.b_download.panel.cpu.CPUPercentageLinePanel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class ResourceUsagePanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -8057463059748579417L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    @Autowired
    private CPUPercentageLinePanel cpuPercentageLinePanel;

    private final JLabel threadCountValueLabel;
    private final JLabel downloadSpeedValueLabel;
    private final JLabel downloadPerSecCountValueLabel;
    private final JLabel systemCpuUsageValueLabel;
    private final JLabel processCpuUsageValueLabel;
    private final JPanel cpuPercPanel;

    public ResourceUsagePanel() {

        /* label */
        var tablePanel = new JPanel();

        var threadCountTitleLabel = new JLabel("下载线程数：");
        threadCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        this.threadCountValueLabel = new JLabel("0");
        this.threadCountValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        var downloadSpeedTitleLabel = new JLabel("下载速度：");
        downloadSpeedTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        this.downloadSpeedValueLabel = new JLabel("0B/s");
        this.downloadSpeedValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        var downloadPerSecCountTitleLabel = new JLabel("每秒下载量：");
        downloadPerSecCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        this.downloadPerSecCountValueLabel = new JLabel("0");
        this.downloadPerSecCountValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        GroupLayout gl_tablePanel = new GroupLayout(tablePanel);
        gl_tablePanel.setHorizontalGroup(
                gl_tablePanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_tablePanel.createSequentialGroup()
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_tablePanel.createSequentialGroup()
                                                .addComponent(threadCountTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.threadCountValueLabel))
                                        .addGroup(gl_tablePanel.createSequentialGroup()
                                                .addComponent(downloadSpeedTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.downloadSpeedValueLabel))
                                        .addGroup(gl_tablePanel.createSequentialGroup()
                                                .addComponent(downloadPerSecCountTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.downloadPerSecCountValueLabel)))
                                .addContainerGap(40, Short.MAX_VALUE))
        );
        gl_tablePanel.setVerticalGroup(
                gl_tablePanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_tablePanel.createSequentialGroup()
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(threadCountTitleLabel)
                                        .addComponent(this.threadCountValueLabel))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(downloadSpeedTitleLabel)
                                        .addComponent(this.downloadSpeedValueLabel))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(downloadPerSecCountTitleLabel)
                                        .addComponent(this.downloadPerSecCountValueLabel))
                                .addContainerGap(18, Short.MAX_VALUE))
        );
        tablePanel.setLayout(gl_tablePanel);
        /* label */

        /* 折线图 */
        this.cpuPercPanel = new JPanel();
        this.cpuPercPanel.setLayout(new BorderLayout(0, 0));
        /* 折线图 */

        /* CPU使用率文字 */
        var cpuUsageTextPanel = new JPanel();

        var systemCpuUsageTitleLabel = new JLabel("系统CPU使用率");
        systemCpuUsageTitleLabel.setForeground(new Color(51, 102, 204));
        systemCpuUsageTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        this.systemCpuUsageValueLabel = new JLabel("0.0%");
        this.systemCpuUsageValueLabel.setForeground(new Color(51, 102, 204));
        this.systemCpuUsageValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        var processCpuUsageTitleLabel = new JLabel("程序CPU使用率");
        processCpuUsageTitleLabel.setForeground(new Color(255, 102, 0));
        processCpuUsageTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        this.processCpuUsageValueLabel = new JLabel("0.0%");
        this.processCpuUsageValueLabel.setForeground(new Color(255, 102, 0));
        this.processCpuUsageValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gl_cpuUsageTextPanel = new GroupLayout(cpuUsageTextPanel);
        gl_cpuUsageTextPanel.setHorizontalGroup(gl_cpuUsageTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_cpuUsageTextPanel.createSequentialGroup().addContainerGap(20, Short.MAX_VALUE)
                        .addGroup(gl_cpuUsageTextPanel.createParallelGroup(Alignment.LEADING)
                                .addComponent(systemCpuUsageTitleLabel, Alignment.TRAILING)
                                .addComponent(this.systemCpuUsageValueLabel, Alignment.TRAILING)
                                .addComponent(processCpuUsageTitleLabel, Alignment.TRAILING)
                                .addComponent(this.processCpuUsageValueLabel, Alignment.TRAILING))));
        gl_cpuUsageTextPanel.setVerticalGroup(gl_cpuUsageTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_cpuUsageTextPanel.createSequentialGroup().addComponent(systemCpuUsageTitleLabel)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(this.systemCpuUsageValueLabel)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(processCpuUsageTitleLabel)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(this.processCpuUsageValueLabel)
                        .addContainerGap(18, Short.MAX_VALUE)));
        cpuUsageTextPanel.setLayout(gl_cpuUsageTextPanel);
        /* CPU使用率文字 */

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tablePanel, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addComponent(cpuUsageTextPanel, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(cpuPercPanel, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(cpuPercPanel, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                                        .addComponent(cpuUsageTextPanel, 0, 0, Short.MAX_VALUE)
                                                        .addComponent(tablePanel, GroupLayout.PREFERRED_SIZE, 110, Short.MAX_VALUE))))
                                .addContainerGap(140, Short.MAX_VALUE))
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {
        this.cpuPercPanel.add(this.cpuPercentageLinePanel, BorderLayout.CENTER);
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
        this.client.<String>sub(Topic.RESOURCE_USAGE_THREAD_COUNT, (res) -> {
            SwingUtilities.invokeLater(() -> threadCountValueLabel.setText(res));
        });
        this.client.<String>sub(Topic.RESOURCE_USAGE_DOWNLOAD_SPEED, (res) -> {
            SwingUtilities.invokeLater(() -> downloadSpeedValueLabel.setText(res));
        });
        this.client.<String>sub(Topic.RESOURCE_USAGE_DOWNLOAD_PER_SEC_COUNT, (res) -> {
            SwingUtilities.invokeLater(() -> downloadPerSecCountValueLabel.setText(res));
        });
        this.client.<String>sub(Topic.RESOURCE_USAGE_SYSTEM_CPU_USAGE, (res) -> {
            SwingUtilities.invokeLater(() -> systemCpuUsageValueLabel.setText(res));
        });
        this.client.<String>sub(Topic.RESOURCE_USAGE_PROCESS_CPU_USAGE, (res) -> {
            SwingUtilities.invokeLater(() -> processCpuUsageValueLabel.setText(res));
        });
        this.client.sub(Topic.RESOURCE_USAGE_CLEAR, (res) -> {
            SwingUtilities.invokeLater(() -> {
                threadCountValueLabel.setText("0");
                downloadSpeedValueLabel.setText("0B/s");
                downloadPerSecCountValueLabel.setText("0");
                systemCpuUsageValueLabel.setText("0.0%");
                processCpuUsageValueLabel.setText("0.0%");
            });
        });
    }

}

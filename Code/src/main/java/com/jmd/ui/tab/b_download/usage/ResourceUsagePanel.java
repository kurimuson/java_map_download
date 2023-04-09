package com.jmd.ui.tab.b_download.usage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.Serial;

import javax.swing.*;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
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

    private JLabel threadCountValueLabel;
    private JLabel downloadSpeedValueLabel;
    private JLabel downloadPerSecCountValueLabel;
    private JLabel systemCpuUsageValueLabel;
    private JLabel processCpuUsageValueLabel;

    @PostConstruct
    private void init() {

        /* label */
        JPanel tablePanel = new JPanel();

        JLabel threadCountTitleLabel = new JLabel("下载线程数：");
        threadCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        threadCountValueLabel = new JLabel("0");
        threadCountValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        JLabel downloadSpeedTitleLabel = new JLabel("下载速度：");
        downloadSpeedTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        downloadSpeedValueLabel = new JLabel("0B/s");
        downloadSpeedValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        JLabel downloadPerSecCountTitleLabel = new JLabel("每秒下载量：");
        downloadPerSecCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        downloadPerSecCountValueLabel = new JLabel("0");
        downloadPerSecCountValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        GroupLayout gl_tablePanel = new GroupLayout(tablePanel);
        gl_tablePanel.setHorizontalGroup(
                gl_tablePanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_tablePanel.createSequentialGroup()
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_tablePanel.createSequentialGroup()
                                                .addComponent(threadCountTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(threadCountValueLabel))
                                        .addGroup(gl_tablePanel.createSequentialGroup()
                                                .addComponent(downloadSpeedTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(downloadSpeedValueLabel))
                                        .addGroup(gl_tablePanel.createSequentialGroup()
                                                .addComponent(downloadPerSecCountTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(downloadPerSecCountValueLabel)))
                                .addContainerGap(40, Short.MAX_VALUE))
        );
        gl_tablePanel.setVerticalGroup(
                gl_tablePanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_tablePanel.createSequentialGroup()
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(threadCountValueLabel)
                                        .addComponent(threadCountTitleLabel))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(downloadSpeedTitleLabel)
                                        .addComponent(downloadSpeedValueLabel))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(downloadPerSecCountTitleLabel)
                                        .addComponent(downloadPerSecCountValueLabel))
                                .addContainerGap(18, Short.MAX_VALUE))
        );
        tablePanel.setLayout(gl_tablePanel);
        /* label */

        /* 折线图 */
        JPanel cpuPercPanel = new JPanel();
        cpuPercPanel.setLayout(new BorderLayout(0, 0));
        cpuPercPanel.add(cpuPercentageLinePanel, BorderLayout.CENTER);
        /* 折线图 */

        /* CPU使用率文字 */
        JPanel cpuUsageTextPanel = new JPanel();

        JLabel systemCpuUsageTitleLabel = new JLabel("系统CPU使用率");
        systemCpuUsageTitleLabel.setForeground(new Color(51, 102, 204));
        systemCpuUsageTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        systemCpuUsageValueLabel = new JLabel("0.0%");
        systemCpuUsageValueLabel.setForeground(new Color(51, 102, 204));
        systemCpuUsageValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        JLabel processCpuUsageTitleLabel = new JLabel("程序CPU使用率");
        processCpuUsageTitleLabel.setForeground(new Color(255, 102, 0));
        processCpuUsageTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        processCpuUsageValueLabel = new JLabel("0.0%");
        processCpuUsageValueLabel.setForeground(new Color(255, 102, 0));
        processCpuUsageValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GroupLayout gl_cpuUsageTextPanel = new GroupLayout(cpuUsageTextPanel);
        gl_cpuUsageTextPanel.setHorizontalGroup(gl_cpuUsageTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_cpuUsageTextPanel.createSequentialGroup().addContainerGap(20, Short.MAX_VALUE)
                        .addGroup(gl_cpuUsageTextPanel.createParallelGroup(Alignment.LEADING)
                                .addComponent(systemCpuUsageTitleLabel, Alignment.TRAILING)
                                .addComponent(systemCpuUsageValueLabel, Alignment.TRAILING)
                                .addComponent(processCpuUsageTitleLabel, Alignment.TRAILING)
                                .addComponent(processCpuUsageValueLabel, Alignment.TRAILING))));
        gl_cpuUsageTextPanel.setVerticalGroup(gl_cpuUsageTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_cpuUsageTextPanel.createSequentialGroup().addComponent(systemCpuUsageTitleLabel)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(systemCpuUsageValueLabel)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(processCpuUsageTitleLabel)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(processCpuUsageValueLabel)
                        .addContainerGap(18, Short.MAX_VALUE)));
        cpuUsageTextPanel.setLayout(gl_cpuUsageTextPanel);
        /* CPU使用率文字 */

        GroupLayout groupLayout = new GroupLayout(this);
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

package com.jmd.ui.tab.b_download;

import javax.swing.*;

import com.jmd.model.task.TaskStatusEnum;
import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.task.TaskState;
import com.jmd.ui.common.CommonContainerPanel;
import com.jmd.ui.common.CommonDialog;
import com.jmd.ui.tab.b_download.panel.TaskLogPanel;
import com.jmd.ui.tab.b_download.panel.TaskProgressPanel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.task.TaskExecFunc;
import com.jmd.ui.tab.b_download.panel.TileMergeProgressPanel;
import com.jmd.ui.tab.b_download.panel.TaskStatusPanel;
import com.jmd.ui.tab.b_download.panel.ResourceUsagePanel;

import javax.swing.GroupLayout.Alignment;
import java.io.Serial;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class DownloadTaskPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 7654815968814511149L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    @Autowired
    private TaskStatusPanel taskStatusPanel;
    @Autowired
    private ResourceUsagePanel resourceUsagePanel;
    @Autowired
    private TileMergeProgressPanel tileMergeProgressPanel;
    @Autowired
    private TaskLogPanel taskLogPanel;
    @Autowired
    private TaskProgressPanel taskProgressPanel;

    @Autowired
    private TaskExecFunc taskExec;

    private final CommonContainerPanel taskPanel;
    private final CommonContainerPanel usagePanel;
    private final CommonContainerPanel mergePanel;
    private final CommonContainerPanel logPanel;
    private final CommonContainerPanel progressPanel;
    private final JButton pauseButton;
    private final JButton cancelButton;

    public DownloadTaskPanel() {

        this.taskPanel = new CommonContainerPanel("任务状态");
        this.usagePanel = new CommonContainerPanel("资源使用量");
        this.mergePanel = new CommonContainerPanel("瓦片图合并进度");
        this.logPanel = new CommonContainerPanel("任务日志");
        this.progressPanel = new CommonContainerPanel("下载进度");

        this.pauseButton = new JButton("暂停任务");
        this.pauseButton.setEnabled(false);
        this.pauseButton.setFocusable(false);
        this.pauseButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        this.cancelButton = new JButton("取消任务");
        this.cancelButton.setEnabled(false);
        this.cancelButton.setFocusable(false);
        this.cancelButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.taskPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(this.usagePanel, Alignment.TRAILING, 450, 450, 450)
                                                        .addComponent(this.mergePanel, Alignment.TRAILING, 450, 450, 450)))
                                        .addComponent(this.logPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.progressPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.pauseButton)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.cancelButton)))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(this.taskPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.usagePanel, 155, 155, 155)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.mergePanel, 155, 155, 155)))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.logPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.progressPanel, 67, 67, 67)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(this.cancelButton)
                                        .addComponent(this.pauseButton))
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {

        /* 任务状态 */
        this.taskPanel.addContent(this.taskStatusPanel);
        /* 任务状态 */

        /* 资源使用量 */
        this.usagePanel.addContent(this.resourceUsagePanel);
        /* 资源使用量 */

        /* 瓦片图合并进度 */
        this.mergePanel.addContent(this.tileMergeProgressPanel);
        /* 瓦片图合并进度 */

        /* 任务日志 */
        this.logPanel.addContent(this.taskLogPanel);
        /* 任务日志 */

        /* 下载进度 */
        this.progressPanel.addContent(taskProgressPanel);
        /* 下载进度 */

        /* 暂停任务 */
        this.pauseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && pauseButton.isEnabled()) {
                    if (!TaskState.IS_TASKING) {
                        return;
                    }
                    if (TaskState.IS_PAUSING) {
                        taskExec.taskContinue();
                    } else {
                        taskExec.taskPause();
                    }
                }
            }
        });
        /* 暂停任务 */

        /* 取消任务 */
        this.cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && cancelButton.isEnabled()) {
                    if (!TaskState.IS_TASKING) {
                        return;
                    }
                    var f = CommonDialog.confirm("确认", "是否取消当前任务");
                    if (f) {
                        taskExec.taskCancel();
                    }
                }
            }
        });
        /* 取消任务 */

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
        this.client.<TaskStatusEnum>sub(Topic.TASK_STATUS_ENUM, (res) -> {
            SwingUtilities.invokeLater(() -> {
                switch (res) {
                    // 任务开始
                    case START -> {
                        this.pauseButton.setText("暂停任务");
                        this.pauseButton.setEnabled(true);
                        this.cancelButton.setEnabled(true);
                    }
                    // 任务继续
                    case CONTINUE -> {
                        this.pauseButton.setText("暂停任务");
                    }
                    // 任务暂停
                    case PAUSE -> {
                        this.pauseButton.setText("继续任务");
                    }
                    // 任务结束
                    case FINISH -> {
                        this.pauseButton.setText("暂停任务");
                        this.pauseButton.setEnabled(false);
                        this.cancelButton.setEnabled(false);
                    }
                    // 任务取消
                    case CANCEL -> {
                        this.pauseButton.setEnabled(false);
                        this.cancelButton.setEnabled(false);
                    }
                }
            });
        });
    }

}

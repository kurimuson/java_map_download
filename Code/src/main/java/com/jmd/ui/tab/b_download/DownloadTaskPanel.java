package com.jmd.ui.tab.b_download;

import javax.swing.*;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.taskfunc.TaskState;
import com.jmd.ui.common.CommonContainerPanel;
import com.jmd.ui.common.CommonDialog;
import com.jmd.ui.tab.b_download.log.TaskLogPanel;
import com.jmd.ui.tab.b_download.progress.TaskProgressPanel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.taskfunc.TaskExecFunc;
import com.jmd.ui.tab.b_download.merge.TileMergeProgressPanel;
import com.jmd.ui.tab.b_download.task.TaskStatusPanel;
import com.jmd.ui.tab.b_download.usage.ResourceUsagePanel;

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

    private JButton pauseButton;
    private JButton cancelButton;

//    public DownloadTaskPanel() {
//        init();
//    }

    @PostConstruct
    private void init() {

        /* 任务状态 */
        var taskPanel = new CommonContainerPanel("任务状态");
        taskPanel.addContent(taskStatusPanel);
        /* 任务状态 */

        /* 资源使用量 */
        var usagePanel = new CommonContainerPanel("资源使用量");
        usagePanel.addContent(resourceUsagePanel);
        /* 资源使用量 */

        /* 瓦片图合并进度 */
        var mergePanel = new CommonContainerPanel("瓦片图合并进度");
        mergePanel.addContent(tileMergeProgressPanel);
        /* 瓦片图合并进度 */

        /* 任务日志 */
        var logPanel = new CommonContainerPanel("任务日志");
        logPanel.addContent(taskLogPanel);
        /* 任务日志 */

        /* 下载进度 */
        var progressPanel = new CommonContainerPanel("下载进度");
        progressPanel.addContent(taskProgressPanel);
        /* 下载进度 */

        pauseButton = new JButton("暂停任务");
        pauseButton.setEnabled(false);
        pauseButton.setFocusable(false);
        pauseButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        pauseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && pauseButton.isEnabled()) {
                    if (!TaskState.IS_TASKING) {
                        return;
                    }
                    taskExec.pauseTask();
                }
            }
        });

        cancelButton = new JButton("取消任务");
        cancelButton.setEnabled(false);
        cancelButton.setFocusable(false);
        cancelButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && cancelButton.isEnabled()) {
                    if (!TaskState.IS_TASKING) {
                        return;
                    }
                    var f = CommonDialog.confirm("确认", "是否取消当前任务");
                    if (f) {
                        taskExec.cancelTaks();
                    }
                }
            }
        });

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(taskPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(usagePanel, Alignment.TRAILING, 450, 450, 450)
                                                        .addComponent(mergePanel, Alignment.TRAILING, 450, 450, 450)))
                                        .addComponent(logPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(progressPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(pauseButton)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(cancelButton)))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(taskPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(usagePanel, 155, 155, 155)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(mergePanel, 155, 155, 155)))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(logPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(progressPanel, 67, 67, 67)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(cancelButton)
                                        .addComponent(pauseButton))
                                .addContainerGap())
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
        this.client.<String>sub(Topic.DOWNLOAD_CONSOLE_CANCEL_BUTTON_TEXT, (res) -> {
            SwingUtilities.invokeLater(() -> cancelButton.setText(res));
        });
        this.client.<Boolean>sub(Topic.DOWNLOAD_CONSOLE_CANCEL_BUTTON_STATE, (res) -> {
            SwingUtilities.invokeLater(() -> cancelButton.setEnabled(res));
        });
        this.client.<String>sub(Topic.DOWNLOAD_CONSOLE_PAUSE_BUTTON_TEXT, (res) -> {
            SwingUtilities.invokeLater(() -> pauseButton.setText(res));
        });
        this.client.<Boolean>sub(Topic.DOWNLOAD_CONSOLE_PAUSE_BUTTON_STATE, (res) -> {
            SwingUtilities.invokeLater(() -> pauseButton.setEnabled(res));
        });
    }

}

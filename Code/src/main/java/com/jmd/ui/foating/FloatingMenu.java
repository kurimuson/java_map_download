package com.jmd.ui.foating;

import com.jmd.Application;
import com.jmd.common.StaticVar;
import com.jmd.model.task.TaskStatusEnum;
import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.task.TaskExecFunc;
import com.jmd.task.TaskState;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;

@Component
public class FloatingMenu extends JPopupMenu {

    @Serial
    private static final long serialVersionUID = -2780470099232748691L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    @Autowired
    private TaskExecFunc taskExec;

    private final JMenuItem showMenuItem;
    private final JMenuItem hideMenuItem;
    private final JMenuItem pauseMenuItem;
    private final JMenuItem continueMenuItem;
    private final JMenuItem exitMenuItem;

    public FloatingMenu() {

        this.showMenuItem = new JMenuItem("显示界面");
        this.showMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(this.showMenuItem);

        this.hideMenuItem = new JMenuItem("隐藏界面");
        this.hideMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(this.hideMenuItem);

        this.addSeparator();

        this.pauseMenuItem = new JMenuItem("暂停下载");
        this.pauseMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(this.pauseMenuItem);

        this.continueMenuItem = new JMenuItem("继续下载");
        this.continueMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(this.continueMenuItem);

        this.addSeparator();

        this.exitMenuItem = new JMenuItem("退出");
        this.exitMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(this.exitMenuItem);

    }

    @PostConstruct
    private void init() {
        this.pauseMenuItem.setEnabled(false);
        this.continueMenuItem.setEnabled(false);
        this.showMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    innerMqService.pub(Topic.MAIN_FRAME_SHOW, true);
                }
            }
        });
        this.hideMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    innerMqService.pub(Topic.MAIN_FRAME_HIDE, true);
                }
            }
        });
        this.pauseMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1 && pauseMenuItem.isEnabled()) {
                    if (!TaskState.IS_PAUSING) {
                        taskExec.taskPause();
                    }
                    setVisible(false);
                }
            }
        });
        this.continueMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1 && continueMenuItem.isEnabled()) {
                    if (TaskState.IS_PAUSING) {
                        taskExec.taskContinue();
                    }
                    setVisible(false);
                }
            }
        });
        this.exitMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    Application.exit();
                }
            }
        });
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
                    // 任务开始，任务继续
                    case START, CONTINUE -> {
                        this.pauseMenuItem.setEnabled(true);
                        this.continueMenuItem.setEnabled(false);
                    }
                    // 任务暂停
                    case PAUSE -> {
                        this.pauseMenuItem.setEnabled(false);
                        this.continueMenuItem.setEnabled(true);
                    }
                    // 任务结束，任务取消
                    case FINISH, CANCEL -> {
                        this.pauseMenuItem.setEnabled(false);
                        this.pauseMenuItem.setEnabled(false);
                    }
                }
            });
        });
    }

}

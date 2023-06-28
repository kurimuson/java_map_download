package com.jmd.ui.foating;

import com.jmd.model.task.TaskStatusEnum;
import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.task.TaskState;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;

@Component
public class FloatingWindow extends JWindow {

    @Serial
    private static final long serialVersionUID = 4044639398852146469L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    private final FloatingWindow that;

    private int pressedButton = 0;
    private boolean moved = false;
    private final int width = 100;
    private final int height = 40;
    private int first_x;
    private int first_y;

    @Autowired
    private FloatingMenu menu;
    @Autowired
    private FloatingContentPanel contentPanel;

    public FloatingWindow() {

        that = this;
        this.setAlwaysOnTop(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        this.setBackground(new Color(0, 0, 0, 0));
        this.setLayout(new BorderLayout());

        this.setSize(this.width, this.height);
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - 300, 125);
        this.setVisible(false);

    }

    @PostConstruct
    private void init() {
        this.add(menu);
        this.add(this.contentPanel, BorderLayout.CENTER);
        this.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressedButton = e.getButton();
                moved = false;
                if (e.getButton() == 1) {
                    first_x = e.getX();
                    first_y = e.getY(); // 记录下位移的初点
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 3 && !moved) {
                    menu.show(that, e.getX(), e.getY());
                }
            }
        });
        this.getContentPane().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (pressedButton == 1) {
                    var x = (int) e.getLocationOnScreen().getX();
                    var y = (int) e.getLocationOnScreen().getY();
                    setBounds(x - first_x, y - first_y, width, height);
                    moved = true;
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
    private void destroy() {
        this.innerMqService.destroyClient(this.client);
    }

    public void subInnerMqMessage() throws Exception {
        this.client = this.innerMqService.createClient();
        this.client.<String>sub(Topic.TASK_STATUS_PROGRESS, (res) -> {
            SwingUtilities.invokeLater(() -> {
                this.contentPanel.progressValueLabel.setText(res);
                if (this.isVisible()) {
                    this.repaint();
                }
            });
        });
        this.client.<String>sub(Topic.RESOURCE_USAGE_DOWNLOAD_SPEED, (res) -> {
            SwingUtilities.invokeLater(() -> {
                if (!TaskState.IS_PAUSING) {
                    this.contentPanel.downloadSpeedValueLabel.setText(res);
                    if (this.isVisible()) {
                        this.repaint();
                    }
                }
            });
        });
        this.client.<TaskStatusEnum>sub(Topic.TASK_STATUS_ENUM, (res) -> {
            SwingUtilities.invokeLater(() -> {
                switch (res) {
                    case FINISH -> this.contentPanel.downloadSpeedValueLabel.setText("下载完成");
                    case PAUSE -> this.contentPanel.downloadSpeedValueLabel.setText("暂停下载");
                    case CANCEL -> this.contentPanel.downloadSpeedValueLabel.setText("取消下载");
                }
                this.repaint();
            });
        });
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g2d);
    }

}

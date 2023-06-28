package com.jmd.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serial;

import javax.swing.*;

import com.jmd.Application;
import com.jmd.ApplicationSetting;
import com.jmd.ApplicationStore;
import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.ui.foating.FloatingWindow;
import com.jmd.ui.tab.c_tile.TileViewPanel;
import com.jmd.util.CommonUtils;
import com.jmd.util.ImageUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.task.TaskExecFunc;
import com.jmd.ui.tab.a_map.MapControlPanel;
import com.jmd.ui.tab.b_download.DownloadTaskPanel;
import com.jmd.ui.tab.d_syslog.SystemLogPanel;

@Component
public class MainFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = -628803972270259148L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    @Autowired
    private TaskExecFunc taskExec;

    @Autowired
    private FloatingWindow floatingWindow;
    @Autowired
    private MapControlPanel mapControlPanel;
    @Autowired
    private DownloadTaskPanel downloadTaskPanel;
    @Autowired
    private TileViewPanel tileViewPanel;
    @Autowired
    private SystemLogPanel systemLogPanel;
    @Autowired
    private MainMenuBar mainMenuBar;

    private final JTabbedPane tabbedPane;

    public MainFrame() throws IOException {

        ApplicationStore.commonParentFrame = this;

        /* 布局 */
        this.getContentPane().setLayout(new BorderLayout(0, 0));

        /* Tabbed主界面 */
        this.tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        this.tabbedPane.setFocusable(false);
        this.tabbedPane.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        this.getContentPane().add(this.tabbedPane, BorderLayout.CENTER);

        /* 任务栏图标 */
        var image = ImageUtils.getResourceImage("assets/icon/map.png");
        if (CommonUtils.isMac()) {
            var taskbar = Taskbar.getTaskbar();
            try {
                taskbar.setIconImage(image);
            } catch (UnsupportedOperationException e) {
                System.out.println("The os does not support: 'taskbar.setIconImage'");
            } catch (SecurityException e) {
                System.out.println("There was a security exception for: 'taskbar.setIconImage'");
            }
        } else {
            this.setIconImage(image);
        }


        this.setTitle("地图下载器");
        this.setSize(new Dimension(1280, 720));
        this.setMinimumSize(new Dimension(1150, 650));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
        this.setVisible(false);
        this.setResizable(true);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ApplicationStore.MAIN_FRAME_HEIGHT = e.getComponent().getHeight();
                ApplicationStore.MAIN_FRAME_WIDTH = e.getComponent().getWidth();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                ApplicationStore.MAIN_FRAME_LOCATION_X = e.getComponent().getX();
                ApplicationStore.MAIN_FRAME_LOCATION_Y = e.getComponent().getY();
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!SystemTray.isSupported()) {
                    Application.exit();
                }
            }
        });

    }

    @PostConstruct
    private void init() {

        /* Menu菜单 */
        this.setJMenuBar(this.mainMenuBar);

        /* Tabbed主界面 */
        this.tabbedPane.addTab("地图操作", null, mapControlPanel, null);
        this.tabbedPane.addTab("下载任务", null, downloadTaskPanel, null);
        this.tabbedPane.addTab("瓦片预览", null, tileViewPanel, null);
        this.tabbedPane.addTab("系统日志", null, systemLogPanel, null);

        /* 悬浮窗 */
        if (ApplicationSetting.getSetting().getFloatingWindowShow()) {
            this.floatingWindow.setVisible(true);
        }

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
        client.sub(Topic.UPDATE_UI, (res) -> {
            SwingUtilities.invokeLater(() -> {
                SwingUtilities.updateComponentTreeUI(this);
            });
        });
        client.sub(Topic.MAIN_FRAME_SELECTED_INDEX, this.tabbedPane::setSelectedIndex);
        client.sub(Topic.FLOATING_WINDOW_TOGGLE, (res) -> {
            SwingUtilities.invokeLater(() -> {
                this.floatingWindow.setVisible(!this.floatingWindow.isVisible());
            });
        });
        client.sub(Topic.MAIN_FRAME_SHOW, (res) -> {
            SwingUtilities.invokeLater(() -> {
                this.setVisible(true);
            });
        });
        client.sub(Topic.MAIN_FRAME_HIDE, (res) -> {
            SwingUtilities.invokeLater(() -> {
                this.setVisible(false);
            });
        });
    }

}

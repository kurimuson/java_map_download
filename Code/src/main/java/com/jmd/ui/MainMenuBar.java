package com.jmd.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.Serial;
import java.util.HashMap;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;

import com.jmd.ApplicationSetting;
import com.jmd.browser.BrowserEngine;
import com.jmd.common.WsSendTopic;
import com.jmd.rx.Topic;
import com.jmd.rx.service.InnerMqService;
import com.jmd.taskfunc.TaskState;
import com.jmd.ui.common.CommonDialog;
import com.jmd.ui.frame.info.AboutFrame;
import com.jmd.ui.frame.info.DonateFrame;
import com.jmd.ui.frame.info.LicenseFrame;
import com.jmd.ui.frame.setting.ProxySettingFrame;
import com.jmd.ui.tab.a_map.browser.BrowserPanel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.entity.theme.ThemeEntity;
import com.jmd.taskfunc.TaskExecFunc;
import com.jmd.ui.tab.a_map.bottom.BottomInfoPanel;
import com.jmd.util.TaskUtils;

import javax.swing.ImageIcon;

@Component
public class MainMenuBar extends JMenuBar {

    @Serial
    private static final long serialVersionUID = 6614126656093043485L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();

    @Autowired
    private AboutFrame aboutFrame;
    @Autowired
    private LicenseFrame licenseFrame;
    @Autowired
    private DonateFrame donateFrame;
    @Autowired
    private ProxySettingFrame proxySettingFrame;
    @Autowired
    private TaskExecFunc taskExec;
    @Autowired
    private BrowserPanel browserPanel;
    @Autowired
    private BottomInfoPanel bottomInfoPanel;
    @Autowired
    private BrowserEngine browserEngine;

    private final JMenuItem themeNameLabel = new JMenuItem();
    private final ImageIcon selectedIcon = new ImageIcon(Objects.requireNonNull(MainMenuBar.class.getResource("/com/jmd/assets/icon/selected.png")));

    @PostConstruct
    private void init() {

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        var styleMenu = new JMenu("主题");
        styleMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(styleMenu);

        for (ThemeEntity parent : StaticVar.THEME_LIST) {
            var themeMenu = new JMenu(parent.getName());
            themeMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
            styleMenu.add(themeMenu);
            for (var theme : parent.getSub()) {
                var themeSubMenuItem = new JMenuItem(theme.getName());
                themeSubMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
                themeMenu.add(themeSubMenuItem);
                themeSubMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.getButton() == 1) {
                            var name = parent.getName() + " " + theme.getName();
                            var map = new HashMap<String, Object>();
                            map.put("name", name);
                            map.put("type", theme.getType());
                            map.put("clazz", theme.getClazz());
                            innerMqService.pub(Topic.CHANGE_THEME, map);
                        }
                    }
                });
            }
        }

        var browserMenu = new JMenu("浏览器");
        browserMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(browserMenu);

        var refreshMenuItem = new JMenuItem("刷新");
        refreshMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        browserMenu.add(refreshMenuItem);
        refreshMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    browserEngine.reload();
                }
            }
        });

        var revertMenuItem = new JMenuItem("清除缓存");
        revertMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        browserMenu.add(revertMenuItem);
        revertMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    browserEngine.clearLocalStorage();
                    var f = CommonDialog.confirm("确认", "已清除缓存，是否刷新页面？");
                    if (f) {
                        browserEngine.reload();
                    }
                }
            }
        });

        var consoleMenuItem = new JMenuItem("开发者工具");
        consoleMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        browserMenu.add(consoleMenuItem);
        consoleMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    browserPanel.toggleDevTools();
                    if (browserPanel.isDevToolOpen()) {
                        consoleMenuItem.setIcon(selectedIcon);
                    } else {
                        consoleMenuItem.setIcon(null);
                    }
                }
            }
        });

        var taskMenu = new JMenu("任务");
        taskMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(taskMenu);

        var loadTaskMenuItem = new JMenuItem("导入未完成的下载");
        loadTaskMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        taskMenu.add(loadTaskMenuItem);
        loadTaskMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    if (TaskState.IS_TASKING) {
                        CommonDialog.alert(null, "当前正在进行下载任务");
                        return;
                    }
                    var file = selectTaskFile();
                    if (file != null) {
                        var taskAllInfo = TaskUtils.getExistTaskByFile(file);
                        if (taskAllInfo != null) {
                            innerMqService.pub(Topic.MAIN_FRAME_SELECTED_INDEX, 1);
                            taskExec.loadTask(taskAllInfo);
                        } else {
                            CommonDialog.alert(null, "导入失败，任务文件已损坏");
                        }
                    }
                }
            }
        });

        var downloadAllWorldMenuItem = new JMenuItem("直接下载世界地图");
        downloadAllWorldMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        taskMenu.add(downloadAllWorldMenuItem);
        downloadAllWorldMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    if (TaskState.IS_TASKING) {
                        CommonDialog.alert(null, "当前正在进行下载任务");
                        return;
                    }
                    browserEngine.sendMessageByWebsocket(WsSendTopic.SUBMIT_WORLD_DOWNLOAD, null);
                }
            }
        });

        var networkMenu = new JMenu("网络");
        networkMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(networkMenu);

        var proxyMenuItem = new JMenuItem("代理设置");
        proxyMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        networkMenu.add(proxyMenuItem);
        proxyMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    proxySettingFrame.setVisible(true);
                }
            }
        });

        var otherMenu = new JMenu("其他");
        otherMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(otherMenu);

        var aboutMenuItem = new JMenuItem("关于");
        aboutMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        otherMenu.add(aboutMenuItem);
        aboutMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    aboutFrame.setVisible(true);
                }
            }
        });

        var licenseMenuItem = new JMenuItem("license");
        licenseMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        otherMenu.add(licenseMenuItem);
        licenseMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    licenseFrame.setVisible(true);
                }
            }
        });

        var donateMenuItem = new JMenuItem("捐赠开发者");
        donateMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        otherMenu.add(donateMenuItem);
        donateMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    donateFrame.setVisible(true);
                }
            }
        });

        themeNameLabel.setText("Theme: " + ApplicationSetting.getSetting().getThemeName());
        themeNameLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        themeNameLabel.setFocusable(false);
        themeNameLabel.setEnabled(false);
        this.add(themeNameLabel);

        try {
            this.subInnerMqMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void subInnerMqMessage() throws Exception {
        var client = this.innerMqService.createClient();
        client.<String>sub(Topic.UPDATE_THEME_TEXT, (res) -> {
            themeNameLabel.setText("Theme: " + res);
        });
    }

    private File selectTaskFile() {
        var chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "地图下载任务(*.jmd)";
            }

            @Override
            public boolean accept(File f) {
                var end = f.getName().toLowerCase();
                return end.endsWith(".jmd") || f.isDirectory();
            }
        });
        chooser.setDialogTitle("选择未完成的下载任务...");
        chooser.setApproveButtonText("导入");
        chooser.setMultiSelectionEnabled(true);
        chooser.showOpenDialog(null);
        return chooser.getSelectedFile();
    }

}

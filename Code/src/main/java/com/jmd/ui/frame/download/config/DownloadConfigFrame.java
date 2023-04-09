package com.jmd.ui.frame.download.config;

import java.awt.*;
import javax.swing.*;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.taskfunc.TaskState;
import com.jmd.ui.common.CommonContainerPanel;
import com.jmd.ui.common.CommonDialog;
import com.jmd.ui.common.CommonSubFrame;
import com.jmd.ui.frame.download.preview.DownloadPreviewFrame;
import com.jmd.ui.frame.download.config.panel.OtherSettingPanel;
import com.jmd.ui.frame.download.config.panel.PathSelectorPanel;
import com.jmd.ui.frame.download.config.panel.ZoomSelectorPanel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.entity.geo.Polygon;
import com.jmd.entity.task.TaskCreateEntity;
import com.jmd.taskfunc.TaskExecFunc;
import com.jmd.ui.MainFrame;
import com.jmd.util.TaskUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class DownloadConfigFrame extends CommonSubFrame {

    @Serial
    private static final long serialVersionUID = 394597878094632313L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    @Autowired
    private TaskExecFunc taskExec;
    @Autowired
    private MainFrame mainFrame;
    @Autowired
    private DownloadPreviewFrame downloadPreviewFrame;

    @Autowired
    private ZoomSelectorPanel zoomSelectorPanel;
    @Autowired
    private OtherSettingPanel otherSettingPanel;
    @Autowired
    private PathSelectorPanel pathSelectorPanel;

    private String url;
    private List<Polygon> polygons;
    private String tileName;
    private String mapType;

    private JButton previewButton;
    private JButton downloadButton;

//    public DownloadConfigFrame() {
//        init();
//    }

    @PostConstruct
    private void init() {

        // 层级选择
        var zoomPanel = new CommonContainerPanel("层级选择");
        zoomPanel.addContent(zoomSelectorPanel);

        // 其他设置
        var otherPanel = new CommonContainerPanel("其他设置");
        otherPanel.addContent(otherSettingPanel);

        // 路径选择
        var pathPanel = new CommonContainerPanel("路径选择");
        pathPanel.addContent(pathSelectorPanel);

        this.previewButton = new JButton("预估下载量");
        this.previewButton.setFocusable(false);
        this.previewButton.setEnabled(false);
        this.previewButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.previewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1 && previewButton.isEnabled()) {
                    downloadPreviewFrame.showPreview(zoomSelectorPanel.getSelectedZooms(), polygons);
                }
            }
        });

        this.downloadButton = new JButton("下载");
        this.downloadButton.setFocusable(false);
        this.downloadButton.setEnabled(false);
        this.downloadButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        /* 开始下载按钮点击事件 */
        this.downloadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1 && downloadButton.isEnabled()) {
                    if (TaskState.IS_TASKING) {
                        CommonDialog.alert(null, "当前正在进行下载任务");
                        return;
                    }
                    boolean isCreate = true;
                    var path = pathSelectorPanel.getSelectedDirPath().getAbsolutePath();
                    if (isTaskExist(path)) {
                        String[] options = {"导入任务", "创建新任务"};
                        var n = CommonDialog.option("选择", "该目录下已存在下载任务，请选择导入任务或新建任务", options);
                        isCreate = (n != null && n == 1);
                    }
                    if (isCreate) {
                        // 创建新任务
                        innerMqService.pub(Topic.MAIN_FRAME_SELECTED_INDEX, 1);
                        taskExec.createTask(getTaskCreate());
                    } else {
                        // 导入现有任务
                        var taskAllInfo = TaskUtils.getExistTaskByPath(path);
                        if (null != taskAllInfo) {
                            innerMqService.pub(Topic.MAIN_FRAME_SELECTED_INDEX, 1);
                            taskExec.loadTask(taskAllInfo);
                        } else {
                            var f = CommonDialog.confirm("选择", "读取现有任务失败，这将创建新的下载任务");
                            if (f) {
                                innerMqService.pub(Topic.MAIN_FRAME_SELECTED_INDEX, 1);
                                taskExec.createTask(getTaskCreate());
                            } else {
                                CommonDialog.alert(null, "用户取消创建下载任务");
                                return;
                            }
                        }
                    }
                    setVisible(false);
                }
            }
        });

        var cancelButton = new JButton("取消");
        cancelButton.setFocusable(false);
        cancelButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(zoomPanel, 100, 120, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(otherPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                        .addComponent(pathPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                                                .addComponent(previewButton, 100, 100, 100)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(downloadButton, 100, 100, 100)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(cancelButton, 100, 100, 100)))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(zoomPanel, 365, 365, 365)
                                        .addComponent(otherPanel, 365, 365, 365))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(pathPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(cancelButton)
                                        .addComponent(downloadButton)
                                        .addComponent(previewButton))
                                .addContainerGap())
        );
        getContentPane().setLayout(groupLayout);
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setVisible(false);
            }
        });

        this.setTitle("下载设置");
        this.setSize(new Dimension(480, 650));
        this.setVisible(false);
        this.setResizable(false);

        try {
            this.subInnerMqMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PreDestroy
    protected void destroy() {
        super.destroy();
        this.innerMqService.destroyClient(this.client);
    }

    private void subInnerMqMessage() throws Exception {
        this.client = this.innerMqService.createClient();
        this.client.<Boolean>sub(Topic.DOWNLOAD_CONFIG_FRAME_ZOOM_SELECTED, (res) -> {
            SwingUtilities.invokeLater(() -> {
                this.previewButton.setEnabled(res && this.pathSelectorPanel.isHasSelected());
                this.downloadButton.setEnabled(res && this.pathSelectorPanel.isHasSelected());
            });
        });
        this.client.<Boolean>sub(Topic.DOWNLOAD_CONFIG_FRAME_PATH_SELECTED, (res) -> {
            SwingUtilities.invokeLater(() -> {
                this.previewButton.setEnabled(res && this.pathSelectorPanel.isHasSelected());
                this.downloadButton.setEnabled(res && this.pathSelectorPanel.isHasSelected());
            });
        });
    }

    // 创建任务，打开面板
    public void createNewTask(String url, List<Polygon> polygons, String tileName, String mapType) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            this.setVisible(true);
        });
        this.zoomSelectorPanel.removeAllSelectedZooms();
        this.previewButton.setEnabled(false);
        this.downloadButton.setEnabled(false);
        this.url = url;
        this.polygons = polygons;
        this.tileName = tileName;
        this.mapType = mapType;
    }

    // 创建任务实例类
    private TaskCreateEntity getTaskCreate() {
        TaskCreateEntity taskCreate = new TaskCreateEntity();
        taskCreate.setTileUrl(this.url);
        taskCreate.setTileName(this.tileName);
        taskCreate.setMapType(this.mapType);
        taskCreate.setSavePath(this.pathSelectorPanel.getSelectedDirPath().getAbsolutePath());
        taskCreate.setZoomList(this.zoomSelectorPanel.getSelectedZooms());
        taskCreate.setPolygons(polygons);
        taskCreate.setPathStyle(this.otherSettingPanel.getPathStyle());
        taskCreate.setImgType(this.otherSettingPanel.getImgType());
        taskCreate.setIsCoverExists(this.otherSettingPanel.isCoverExist());
        taskCreate.setIsMergeTile(this.otherSettingPanel.isMergeTile());
        taskCreate.setMergeType(this.otherSettingPanel.getMergeType());
        return taskCreate;
    }

    private boolean isTaskExist(String path) {
        File file = new File(path + "/task_info.jmd");
        return file.exists() && file.isFile();
    }

}

package com.jmd.ui.tab.b_download.task;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serial;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;

@Component
public class TaskStatusPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 1609822863496587388L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    private JLabel currentContentLabel;
    private JLabel mapTypeContentLabel;
    private JLabel zoomsContentLabel;
    private JLabel imgTypeContentLabel;
    private JLabel savePathContentLabel;
    private JLabel pathStyleContentLabel;
    private JLabel isCoverExistContentLabel;
    private JLabel tileAllCountContentLabel;
    private JLabel tileDownloadedCountContentLabel;
    private JLabel progressContentLabel;

//	public TaskStatusPanel() {
//		init();
//	}

    @PostConstruct
    private void init() {

        var gbl_this = new GridBagLayout();
        gbl_this.columnWidths = new int[]{0, 0, 0, 0};
        gbl_this.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_this.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_this.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        this.setLayout(gbl_this);

        /* 当前任务 */
        var currentTitleLabel = new JLabel("当前任务：");
        currentTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_currentTitleLabel = new GridBagConstraints();
        gbc_currentTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_currentTitleLabel.gridx = 0;
        gbc_currentTitleLabel.gridy = 0;
        this.add(currentTitleLabel, gbc_currentTitleLabel);

        currentContentLabel = new JLabel("");
        currentContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_currentContentLabel = new GridBagConstraints();
        gbc_currentContentLabel.anchor = GridBagConstraints.WEST;
        gbc_currentContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_currentContentLabel.gridx = 1;
        gbc_currentContentLabel.gridy = 0;
        this.add(currentContentLabel, gbc_currentContentLabel);
        /* 当前任务 */

        /* 地图类型 */
        JLabel mapTypeTitleLabel = new JLabel("地图类型：");
        mapTypeTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_mapTypeTitleLabel = new GridBagConstraints();
        gbc_mapTypeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mapTypeTitleLabel.gridx = 0;
        gbc_mapTypeTitleLabel.gridy = 1;
        this.add(mapTypeTitleLabel, gbc_mapTypeTitleLabel);

        mapTypeContentLabel = new JLabel("");
        mapTypeContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_mapTypeContentLabel = new GridBagConstraints();
        gbc_mapTypeContentLabel.anchor = GridBagConstraints.WEST;
        gbc_mapTypeContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_mapTypeContentLabel.gridx = 1;
        gbc_mapTypeContentLabel.gridy = 1;
        this.add(mapTypeContentLabel, gbc_mapTypeContentLabel);
        /* 地图类型 */

        /* 所选图层 */
        var zoomsTitleLabel = new JLabel("所选层级：");
        zoomsTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_zoomsTitleLabel = new GridBagConstraints();
        gbc_zoomsTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_zoomsTitleLabel.gridx = 0;
        gbc_zoomsTitleLabel.gridy = 2;
        this.add(zoomsTitleLabel, gbc_zoomsTitleLabel);

        zoomsContentLabel = new JLabel("");
        zoomsContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_zoomsContentLabel = new GridBagConstraints();
        gbc_zoomsContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_zoomsContentLabel.anchor = GridBagConstraints.WEST;
        gbc_zoomsContentLabel.gridx = 1;
        gbc_zoomsContentLabel.gridy = 2;
        this.add(zoomsContentLabel, gbc_zoomsContentLabel);
        /* 所选图层 */

        /* 瓦片格式 */
        var imgTypeTitleLabel = new JLabel("瓦片格式：");
        imgTypeTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_imgTypeTitleLabel = new GridBagConstraints();
        gbc_imgTypeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_imgTypeTitleLabel.gridx = 0;
        gbc_imgTypeTitleLabel.gridy = 3;
        this.add(imgTypeTitleLabel, gbc_imgTypeTitleLabel);

        imgTypeContentLabel = new JLabel("");
        imgTypeContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_imgTypeContentLabel = new GridBagConstraints();
        gbc_imgTypeContentLabel.anchor = GridBagConstraints.WEST;
        gbc_imgTypeContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_imgTypeContentLabel.gridx = 1;
        gbc_imgTypeContentLabel.gridy = 3;
        this.add(imgTypeContentLabel, gbc_imgTypeContentLabel);
        /* 瓦片格式 */

        /* 保存路径 */
        var savePathTitleLabel = new JLabel("保存路径：");
        savePathTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_savePathTitleLabel = new GridBagConstraints();
        gbc_savePathTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_savePathTitleLabel.gridx = 0;
        gbc_savePathTitleLabel.gridy = 4;
        this.add(savePathTitleLabel, gbc_savePathTitleLabel);

        savePathContentLabel = new JLabel("");
        savePathContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_savePathContentLabel = new GridBagConstraints();
        gbc_savePathContentLabel.anchor = GridBagConstraints.WEST;
        gbc_savePathContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_savePathContentLabel.gridx = 1;
        gbc_savePathContentLabel.gridy = 4;
        this.add(savePathContentLabel, gbc_savePathContentLabel);
        /* 保存路径 */

        /* 命名风格 */
        var pathStyleTitleLabel = new JLabel("命名风格：");
        pathStyleTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_pathStyleTitleLabel = new GridBagConstraints();
        gbc_pathStyleTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_pathStyleTitleLabel.gridx = 0;
        gbc_pathStyleTitleLabel.gridy = 5;
        this.add(pathStyleTitleLabel, gbc_pathStyleTitleLabel);

        pathStyleContentLabel = new JLabel("");
        pathStyleContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_pathStyleContentLabel = new GridBagConstraints();
        gbc_pathStyleContentLabel.anchor = GridBagConstraints.WEST;
        gbc_pathStyleContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_pathStyleContentLabel.gridx = 1;
        gbc_pathStyleContentLabel.gridy = 5;
        this.add(pathStyleContentLabel, gbc_pathStyleContentLabel);
        /* 命名风格 */

        /* 覆盖下载 */
        var isCoverExistTitleLabel = new JLabel("覆盖下载：");
        isCoverExistTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_isCoverExistTitleLabel = new GridBagConstraints();
        gbc_isCoverExistTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_isCoverExistTitleLabel.gridx = 0;
        gbc_isCoverExistTitleLabel.gridy = 6;
        this.add(isCoverExistTitleLabel, gbc_isCoverExistTitleLabel);

        isCoverExistContentLabel = new JLabel("");
        isCoverExistContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_isCoverExistContentLabel = new GridBagConstraints();
        gbc_isCoverExistContentLabel.anchor = GridBagConstraints.WEST;
        gbc_isCoverExistContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_isCoverExistContentLabel.gridx = 1;
        gbc_isCoverExistContentLabel.gridy = 6;
        this.add(isCoverExistContentLabel, gbc_isCoverExistContentLabel);
        /* 覆盖下载 */

        /* 瓦片总数 */
        var tileAllCountTitleLabel = new JLabel("瓦片总数：");
        tileAllCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_tileAllCountTitleLabel = new GridBagConstraints();
        gbc_tileAllCountTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_tileAllCountTitleLabel.gridx = 0;
        gbc_tileAllCountTitleLabel.gridy = 7;
        this.add(tileAllCountTitleLabel, gbc_tileAllCountTitleLabel);

        tileAllCountContentLabel = new JLabel("");
        tileAllCountContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_tileAllCountContentLabel = new GridBagConstraints();
        gbc_tileAllCountContentLabel.anchor = GridBagConstraints.WEST;
        gbc_tileAllCountContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_tileAllCountContentLabel.gridx = 1;
        gbc_tileAllCountContentLabel.gridy = 7;
        this.add(tileAllCountContentLabel, gbc_tileAllCountContentLabel);
        /* 瓦片总数 */

        /* 已下载数 */
        JLabel tileDownloadedCountTitleLabel = new JLabel("已下载数：");
        tileDownloadedCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_tileDownloadedCountTitleLabel = new GridBagConstraints();
        gbc_tileDownloadedCountTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_tileDownloadedCountTitleLabel.gridx = 0;
        gbc_tileDownloadedCountTitleLabel.gridy = 8;
        this.add(tileDownloadedCountTitleLabel, gbc_tileDownloadedCountTitleLabel);

        tileDownloadedCountContentLabel = new JLabel("");
        tileDownloadedCountContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_tileDownloadedCountContentLabel = new GridBagConstraints();
        gbc_tileDownloadedCountContentLabel.anchor = GridBagConstraints.WEST;
        gbc_tileDownloadedCountContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_tileDownloadedCountContentLabel.gridx = 1;
        gbc_tileDownloadedCountContentLabel.gridy = 8;
        this.add(tileDownloadedCountContentLabel, gbc_tileDownloadedCountContentLabel);
        /* 已下载数 */

        /* 下载进度 */
        JLabel progressTitleLabel = new JLabel("下载进度：");
        progressTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_progressTitleLabel = new GridBagConstraints();
        gbc_progressTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_progressTitleLabel.gridx = 0;
        gbc_progressTitleLabel.gridy = 9;
        this.add(progressTitleLabel, gbc_progressTitleLabel);

        progressContentLabel = new JLabel("");
        progressContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        var gbc_progressContentLabel = new GridBagConstraints();
        gbc_progressContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_progressContentLabel.anchor = GridBagConstraints.WEST;
        gbc_progressContentLabel.gridx = 1;
        gbc_progressContentLabel.gridy = 9;
        this.add(progressContentLabel, gbc_progressContentLabel);
        /* 下载进度 */

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
        this.client.<String>sub(Topic.TASK_STATUS_CURRENT, (res) -> {
            SwingUtilities.invokeLater(() -> currentContentLabel.setText(res));
        });
        this.client.<String>sub(Topic.TASK_STATUS_MAP_TYPE, (res) -> {
            SwingUtilities.invokeLater(() -> mapTypeContentLabel.setText(res));
        });
        this.client.<String>sub(Topic.TASK_STATUS_LAYERS, (res) -> {
            SwingUtilities.invokeLater(() -> zoomsContentLabel.setText(res));
        });
        this.client.<String>sub(Topic.TASK_STATUS_SAVE_PATH, (res) -> {
            SwingUtilities.invokeLater(() -> savePathContentLabel.setText(res));
        });
        this.client.<String>sub(Topic.TASK_STATUS_PATH_STYLE, (res) -> {
            SwingUtilities.invokeLater(() -> pathStyleContentLabel.setText(res));
        });
        this.client.<String>sub(Topic.TASK_STATUS_IS_COVER_EXIST, (res) -> {
            SwingUtilities.invokeLater(() -> isCoverExistContentLabel.setText(res));
        });
        this.client.<String>sub(Topic.TASK_STATUS_IMG_TYPE, (res) -> {
            SwingUtilities.invokeLater(() -> imgTypeContentLabel.setText(res));
        });
        this.client.<String>sub(Topic.TASK_STATUS_TILE_ALL_COUNT, (res) -> {
            SwingUtilities.invokeLater(() -> tileAllCountContentLabel.setText(res));
        });
        this.client.<String>sub(Topic.TASK_STATUS_TILE_DOWNLOADED_COUNT, (res) -> {
            SwingUtilities.invokeLater(() -> tileDownloadedCountContentLabel.setText(res));
        });
        this.client.<String>sub(Topic.TASK_STATUS_PROGRESS, (res) -> {
            SwingUtilities.invokeLater(() -> progressContentLabel.setText(res));
        });
    }

}

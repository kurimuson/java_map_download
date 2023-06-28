package com.jmd.ui.tab.a_map.panel;

import com.jmd.common.StaticVar;
import com.jmd.common.WsSendTopic;
import com.jmd.task.TaskState;
import com.jmd.ui.common.CommonDialog;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class MapControlButtonPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -7566297857880130132L;

    @Autowired
    private MapControlBrowserPanel mapViewBrowserPanel;

    private final JButton zoomInButton;
    private final JButton zoomOutButton;
    private final JButton gridButton;
    private final JButton downloadButton;
    private final JButton panButton;
    private final JButton drawButton;
    private final JButton fitViewButton;
    private final JButton removeButton;

    public MapControlButtonPanel() {

        // zoom +
        this.zoomInButton = new JButton("放大 ＋");
        this.zoomInButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.zoomInButton.setFocusable(false);

        // zoom -
        this.zoomOutButton = new JButton("缩小 －");
        this.zoomOutButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.zoomOutButton.setFocusable(false);

        // 网格
        this.gridButton = new JButton("网格");
        this.gridButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.gridButton.setFocusable(false);

        // 下载
        this.downloadButton = new JButton("下载地图");
        this.downloadButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.downloadButton.setFocusable(false);

        // 拖动
        this.panButton = new JButton("拖动地图");
        this.panButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.panButton.setFocusable(false);

        // 绘制
        this.drawButton = new JButton("绘制图形");
        this.drawButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.drawButton.setFocusable(false);

        // fitView
        this.fitViewButton = new JButton("图形居中");
        this.fitViewButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.fitViewButton.setFocusable(false);

        // 移除
        this.removeButton = new JButton("移除图形");
        this.removeButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.removeButton.setFocusable(false);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.zoomInButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.zoomOutButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.gridButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.downloadButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.panButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.drawButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.fitViewButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.removeButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE, false)
                                        .addComponent(this.zoomInButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.zoomOutButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.gridButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.downloadButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE, false)
                                        .addComponent(this.panButton)
                                        .addComponent(this.drawButton)
                                        .addComponent(this.fitViewButton)
                                        .addComponent(this.removeButton))
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {
        this.zoomInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.ZOOM_IN, null);
            }
        });
        this.zoomOutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.ZOOM_OUT, null);
            }
        });
        this.gridButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.GRID_SWITCH, null);
            }
        });
        this.downloadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (TaskState.IS_TASKING) {
                    CommonDialog.alert(null, "当前正在进行下载任务");
                    return;
                }
                mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SUBMIT_BLOCK_DOWNLOAD, null);
            }
        });
        this.panButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.PAN, null);
            }
        });
        this.drawButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.OPEN_DRAW, null);
            }
        });
        this.fitViewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.FIT_VIEW, null);
            }
        });
        this.removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.REMOVE_SHAPE, null);
            }
        });
    }

}

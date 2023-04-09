package com.jmd.ui.tab.a_map.control;

import com.jmd.browser.BrowserEngine;
import com.jmd.common.StaticVar;
import com.jmd.common.WsSendTopic;
import com.jmd.taskfunc.TaskState;
import com.jmd.ui.common.CommonDialog;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
    private BrowserEngine browserEngine;

    @PostConstruct
    private void init() {

        // zoom +
        var zoomInButton = new JButton("放大 ＋");
        zoomInButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        zoomInButton.setFocusable(false);
        zoomInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                browserEngine.sendMessageByWebsocket(WsSendTopic.ZOOM_IN, null);
            }
        });

        // zoom -
        var zoomOutButton = new JButton("缩小 －");
        zoomOutButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        zoomOutButton.setFocusable(false);
        zoomOutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                browserEngine.sendMessageByWebsocket(WsSendTopic.ZOOM_OUT, null);
            }
        });

        // 网格
        var gridButton = new JButton("网格");
        gridButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        gridButton.setFocusable(false);
        gridButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                browserEngine.sendMessageByWebsocket(WsSendTopic.GRID_SWITCH, null);
            }
        });

        // 下载
        var downloadButton = new JButton("下载地图");
        downloadButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        downloadButton.setFocusable(false);
        downloadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (TaskState.IS_TASKING) {
                    CommonDialog.alert(null, "当前正在进行下载任务");
                    return;
                }
                browserEngine.sendMessageByWebsocket(WsSendTopic.SUBMIT_BLOCK_DOWNLOAD, null);
            }
        });

        // 拖动
        var panButton = new JButton("拖动地图");
        panButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        panButton.setFocusable(false);
        panButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                browserEngine.sendMessageByWebsocket(WsSendTopic.PAN, null);
            }
        });

        // 绘制
        var drawButton = new JButton("绘制图形");
        drawButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        drawButton.setFocusable(false);
        drawButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                browserEngine.sendMessageByWebsocket(WsSendTopic.OPEN_DRAW, null);
            }
        });

        // fitview
        var fitviewButton = new JButton("图形居中");
        fitviewButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        fitviewButton.setFocusable(false);
        fitviewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                browserEngine.sendMessageByWebsocket(WsSendTopic.FIT_VIEW, null);
            }
        });

        // 移除
        var removeButton = new JButton("移除图形");
        removeButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        removeButton.setFocusable(false);
        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                browserEngine.sendMessageByWebsocket(WsSendTopic.REMOVE_SHAPE, null);
            }
        });

        var gl_panel = new GroupLayout(this);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addComponent(zoomInButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(zoomOutButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(gridButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(downloadButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addComponent(panButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(drawButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(fitviewButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(removeButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE, false)
                                        .addComponent(zoomInButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(zoomOutButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(gridButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(downloadButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE, false)
                                        .addComponent(panButton)
                                        .addComponent(drawButton)
                                        .addComponent(fitviewButton)
                                        .addComponent(removeButton))
                                .addContainerGap())
        );
        this.setLayout(gl_panel);

    }

}

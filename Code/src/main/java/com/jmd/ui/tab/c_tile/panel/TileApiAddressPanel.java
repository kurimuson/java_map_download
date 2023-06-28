package com.jmd.ui.tab.c_tile.panel;

import com.jmd.ui.common.CommonDialog;
import com.jmd.util.CommonUtils;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.stereotype.Component;

import com.jmd.ApplicationPort;
import com.jmd.common.StaticVar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serial;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class TileApiAddressPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 6755052092289839991L;

    private final String browserViewAddress = "http://localhost:" + ApplicationPort.startPort + "/web/index.html/#/tile-view";
    private final String localApiAddress = "http://localhost:" + ApplicationPort.startPort + "/tile/local?z={z}&x={x}&y={y}";

    private final JButton openBrowserViewAddressButton;
    private final JButton copyLocalApiAddressButton;

    @Setter
    private boolean canView = false;

    public TileApiAddressPanel() {

        this.openBrowserViewAddressButton = new JButton("打开");
        this.openBrowserViewAddressButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.openBrowserViewAddressButton.setFocusable(false);

        this.copyLocalApiAddressButton = new JButton("复制");
        this.copyLocalApiAddressButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.copyLocalApiAddressButton.setFocusable(false);

        var browserViewAddressLabel = new JLabel("在浏览器中查看：" + this.browserViewAddress);
        var localApiAddressLabel = new JLabel("本地XYZ瓦片地址：" + this.localApiAddress);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(localApiAddressLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(browserViewAddressLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(this.openBrowserViewAddressButton, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.copyLocalApiAddressButton, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(this.openBrowserViewAddressButton)
                                        .addComponent(browserViewAddressLabel))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(this.copyLocalApiAddressButton)
                                        .addComponent(localApiAddressLabel))
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {
        this.openBrowserViewAddressButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    if (!canView) {
                        CommonDialog.alert(null, "请选择瓦片路径并加载");
                        return;
                    }
                    try {
                        var desktop = Desktop.getDesktop();
                        desktop.browse(new URI(browserViewAddress));
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        this.copyLocalApiAddressButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    if (!canView) {
                        CommonDialog.alert(null, "请选择瓦片路径并加载");
                        return;
                    }
                    CommonUtils.setClipboardText(localApiAddress);
                    CommonDialog.alert(null, "已复制到剪贴板");
                }
            }
        });
    }

}

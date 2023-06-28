package com.jmd.ui.tab.c_tile;

import com.jmd.model.tile.TileViewParam;
import com.jmd.rx.Topic;
import com.jmd.rx.service.InnerMqService;
import com.jmd.ui.common.CommonContainerPanel;
import com.jmd.ui.common.CommonDialog;
import com.jmd.ui.tab.c_tile.panel.TileApiAddressPanel;
import com.jmd.ui.tab.c_tile.panel.TileImageTypePanel;
import com.jmd.ui.tab.c_tile.panel.TileViewBrowserPanel;
import com.jmd.ui.tab.c_tile.panel.TilePathSelectorPanel;
import com.jmd.util.MyFileUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.Serial;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class TileViewPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -3295266893592422327L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();

    @Autowired
    private TilePathSelectorPanel pathSelectorPanel;
    @Autowired
    private TileImageTypePanel imageTypePanel;
    @Autowired
    private TileApiAddressPanel apiAddressPanel;
    @Autowired
    private TileViewBrowserPanel browserInstPanel;

    private final CommonContainerPanel pathPanel;
    private final CommonContainerPanel typePanel;
    private final CommonContainerPanel apiPanel;
    private final CommonContainerPanel browserPanel;

    public TileViewPanel() {

        this.pathPanel = new CommonContainerPanel("瓦片路径");
        this.typePanel = new CommonContainerPanel("图片类型");
        this.apiPanel = new CommonContainerPanel("本地接口");
        this.browserPanel = new CommonContainerPanel("地图预览");

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.pathPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.typePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.apiPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(this.browserPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(this.pathPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.typePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.apiPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.browserPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {

        /* 瓦片路径 */
        this.pathPanel.addContent(this.pathSelectorPanel);
        /* 瓦片路径 */

        /* 图片类型 */
        this.typePanel.addContent(this.imageTypePanel);
        /* 图片类型 */

        /* 本地接口 */
        this.apiPanel.addContent(this.apiAddressPanel);
        /* 本地接口 */

        /* 浏览器 */
        this.browserPanel.addContent(this.browserInstPanel);
        /* 浏览器 */

        this.pathSelectorPanel.setCallback(() -> {
            var path = this.pathSelectorPanel.getTilePath();
            var pathStyle = this.pathSelectorPanel.getTilePathStyle();
            var type = this.imageTypePanel.getImageType();
            if (path == null) {
                CommonDialog.alert(null, "未选择瓦片路径");
                return;
            }
            if (pathStyle == null) {
                CommonDialog.alert(null, "未选择命名风格");
                return;
            }
            var param = new TileViewParam(MyFileUtils.checkFilePath(path), pathStyle, type);
            this.apiAddressPanel.setCanView(true);
            this.innerMqService.pub(Topic.OPEN_TILE_VIEW, param);
        });

    }

}

package com.jmd.ui.tab.a_map;

import javax.swing.JPanel;

import com.jmd.ui.common.CommonContainerPanel;
import com.jmd.ui.tab.a_map.panel.BottomInfoPanel;
import com.jmd.ui.tab.a_map.panel.MapControlBrowserPanel;
import com.jmd.ui.tab.a_map.panel.MapControlButtonPanel;
import com.jmd.ui.tab.a_map.panel.DistrictSelectorPanel;
import com.jmd.ui.tab.a_map.panel.CustomLayerButtonPanel;
import com.jmd.ui.tab.a_map.panel.DrawTypePanel;
import com.jmd.ui.tab.a_map.panel.LayerSelectorPanel;
import com.jmd.ui.tab.a_map.panel.StatusInfoPanel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serial;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class MapControlPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 7958127591579659670L;

    @Autowired
    private MapControlButtonPanel mapControlButtonPanel;
    @Autowired
    private DistrictSelectorPanel districtSelectorPanel;
    @Autowired
    private StatusInfoPanel statusInfoPanel;
    @Autowired
    private DrawTypePanel drawTypePanel;
    @Autowired
    private LayerSelectorPanel layerSelectorPanel;
    @Autowired
    private CustomLayerButtonPanel customLayerButtonPanel;
    @Autowired
    private MapControlBrowserPanel browserInstPanel;
    @Autowired
    private BottomInfoPanel bottomInfoPanel;

    private final CommonContainerPanel controlPanel;
    private final CommonContainerPanel districtPanel;
    private final CommonContainerPanel statusPanel;
    private final CommonContainerPanel drawPanel;
    private final CommonContainerPanel layerPanel;
    private final CommonContainerPanel customPanel;
    private final CommonContainerPanel bottomPanel;
    private final CommonContainerPanel browserPanel;

    public MapControlPanel() {

        this.controlPanel = new CommonContainerPanel("地图操作");
        this.districtPanel = new CommonContainerPanel("快速选择");
        this.statusPanel = new CommonContainerPanel("状态信息");
        this.drawPanel = new CommonContainerPanel("绘制类型");
        this.layerPanel = new CommonContainerPanel("图层选择");
        this.customPanel = new CommonContainerPanel("自定义");
        this.browserPanel = new CommonContainerPanel("地图预览");
        this.bottomPanel = new CommonContainerPanel(null);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.controlPanel, 300, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.districtPanel, 300, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.statusPanel, 180, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                                        .addComponent(this.drawPanel, 220, 220, 220)
                                                        .addComponent(this.layerPanel, 220, 220, 220)
                                                        .addComponent(this.customPanel, 220, 220, 220))
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.browserPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                        .addComponent(this.bottomPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(this.controlPanel, 50, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.districtPanel, 50, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.statusPanel, 50, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.drawPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.layerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.customPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(this.browserPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.bottomPanel, 10, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {

        /* 地图操作 */
        this.controlPanel.addContent(this.mapControlButtonPanel);
        /* 地图操作 */

        /* 快速选择 */
        this.districtPanel.addContent(this.districtSelectorPanel);
        /* 快速选择 */

        /* 状态信息 */
        this.statusPanel.addContent(this.statusInfoPanel);
        /* 状态信息 */

        /* 绘制类型 */
        this.drawPanel.addContent(this.drawTypePanel);
        /* 绘制类型 */

        /* 图层选择 */
        this.layerPanel.addContent(this.layerSelectorPanel);
        /* 绘制类型 */

        /* 自定义 */
        this.customPanel.addContent(this.customLayerButtonPanel);
        /* 自定义 */

        /* 浏览器 */
        this.browserPanel.addContent(this.browserInstPanel);
        /* 浏览器 */

        /* 底部信息 */
        this.bottomPanel.addContent(this.bottomInfoPanel);
        /* 底部信息 */

    }

}

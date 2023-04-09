package com.jmd.ui.tab.a_map;

import javax.swing.JPanel;

import com.jmd.ui.common.CommonContainerPanel;
import com.jmd.ui.tab.a_map.bottom.BottomInfoPanel;
import com.jmd.ui.tab.a_map.browser.BrowserPanel;
import com.jmd.ui.tab.a_map.control.MapControlButtonPanel;
import com.jmd.ui.tab.a_map.district.DistrictSelectorPanel;
import com.jmd.ui.tab.a_map.custom.CustomLayerButtonPanel;
import com.jmd.ui.tab.a_map.draw.DrawTypePanel;
import com.jmd.ui.tab.a_map.layer.LayerSelectorPanel;
import com.jmd.ui.tab.a_map.status.StatusInfoPanel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serial;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class MapViewPanel extends JPanel {

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
    private BrowserPanel browserPanel;
    @Autowired
    private BottomInfoPanel bottomInfoPanel;

//    public MapViewPanel() {
//        init();
//    }

    @PostConstruct
    private void init() {

        /* 地图操作 */
        var control = new CommonContainerPanel("地图操作");
        control.addContent(mapControlButtonPanel);
        /* 地图操作 */

        /* 快速选择 */
        var district = new CommonContainerPanel("快速选择");
        district.addContent(districtSelectorPanel);
        /* 快速选择 */

        /* 状态信息 */
        var status = new CommonContainerPanel("状态信息");
        status.addContent(statusInfoPanel);
        /* 状态信息 */

        /* 绘制类型 */
        var draw = new CommonContainerPanel("绘制类型");
        draw.addContent(drawTypePanel);
        /* 绘制类型 */

        /* 图层选择 */
        var layer = new CommonContainerPanel("图层选择");
        layer.addContent(layerSelectorPanel);
        /* 绘制类型 */

        /* 自定义 */
        var custom = new CommonContainerPanel("自定义");
        custom.addContent(customLayerButtonPanel);
        /* 自定义 */

        /* 底部信息 */
        var bottom = new CommonContainerPanel(null);
        bottom.addContent(bottomInfoPanel);
        /* 底部信息 */

        /* 浏览器 */
        var browser = new CommonContainerPanel("浏览器");
        browser.addContent(browserPanel);
        /* 浏览器 */

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(control, 300, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(district, 300, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(status, 180, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                                        .addComponent(custom, 220, 220, 220)
                                                        .addComponent(draw, 220, 220, 220)
                                                        .addComponent(layer, 220, 220, 220))
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(browser, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                        .addComponent(bottom, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(control, 50, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(status, 50, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(district, 50, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(draw, 100, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(layer, 100, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(custom, 100, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(browser, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(bottom, 10, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        setLayout(groupLayout);

    }
}

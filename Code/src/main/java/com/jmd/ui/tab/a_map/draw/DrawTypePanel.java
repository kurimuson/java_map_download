package com.jmd.ui.tab.a_map.draw;

import com.jmd.browser.BrowserEngine;
import com.jmd.common.StaticVar;
import com.jmd.common.WsSendTopic;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.Serial;

@Component
public class DrawTypePanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 68092099548468829L;

    @Autowired
    private BrowserEngine browserEngine;

    @PostConstruct
    private void init() {

        var drawTypePolygonRadioButton = new JRadioButton("多边形");
        drawTypePolygonRadioButton.setSelected(true);
        drawTypePolygonRadioButton.setFocusable(false);
        drawTypePolygonRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        drawTypePolygonRadioButton.addItemListener((e) -> {
            if (drawTypePolygonRadioButton == e.getSource() && drawTypePolygonRadioButton.isSelected()) {
                this.browserEngine.sendMessageByWebsocket(WsSendTopic.SWITCH_DRAW_TYPE, "Polygon");
            }
        });

        var drawTypeCircleRadioButton = new JRadioButton("圆形");
        drawTypeCircleRadioButton.setSelected(false);
        drawTypeCircleRadioButton.setFocusable(false);
        drawTypeCircleRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        drawTypeCircleRadioButton.addItemListener((e) -> {
            if (drawTypeCircleRadioButton == e.getSource() && drawTypeCircleRadioButton.isSelected()) {
                this.browserEngine.sendMessageByWebsocket(WsSendTopic.SWITCH_DRAW_TYPE, "Circle");
            }
        });

        var btnGroup = new ButtonGroup();
        btnGroup.add(drawTypePolygonRadioButton);
        btnGroup.add(drawTypeCircleRadioButton);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(drawTypePolygonRadioButton)
                                        .addComponent(drawTypeCircleRadioButton))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(drawTypePolygonRadioButton)
                                .addComponent(drawTypeCircleRadioButton)
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

}

package com.jmd.ui.tab.a_map.panel;

import com.jmd.common.StaticVar;
import com.jmd.common.WsSendTopic;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.Serial;

@Component
public class DrawTypePanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 68092099548468829L;

    @Autowired
    private MapControlBrowserPanel mapViewBrowserPanel;

    private final JRadioButton drawTypePolygonRadioButton;
    private final JRadioButton drawTypeCircleRadioButton;

    public DrawTypePanel() {

        this.drawTypePolygonRadioButton = new JRadioButton("多边形");
        this.drawTypePolygonRadioButton.setSelected(true);
        this.drawTypePolygonRadioButton.setFocusable(false);
        this.drawTypePolygonRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        this.drawTypeCircleRadioButton = new JRadioButton("圆形");
        this.drawTypeCircleRadioButton.setSelected(false);
        this.drawTypeCircleRadioButton.setFocusable(false);
        this.drawTypeCircleRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        var btnGroup = new ButtonGroup();
        btnGroup.add(this.drawTypePolygonRadioButton);
        btnGroup.add(this.drawTypeCircleRadioButton);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(this.drawTypePolygonRadioButton)
                                        .addComponent(this.drawTypeCircleRadioButton))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(this.drawTypePolygonRadioButton)
                                .addComponent(this.drawTypeCircleRadioButton)
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {
        this.drawTypePolygonRadioButton.addItemListener((e) -> {
            if (this.drawTypePolygonRadioButton == e.getSource() && this.drawTypePolygonRadioButton.isSelected()) {
                this.mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_DRAW_TYPE, "Polygon");
            }
        });
        this.drawTypeCircleRadioButton.addItemListener((e) -> {
            if (this.drawTypeCircleRadioButton == e.getSource() && this.drawTypeCircleRadioButton.isSelected()) {
                this.mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_DRAW_TYPE, "Circle");
            }
        });
    }

}

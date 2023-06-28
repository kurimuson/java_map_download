package com.jmd.ui.tab.a_map.panel;

import com.jmd.common.StaticVar;
import com.jmd.rx.Topic;
import com.jmd.rx.service.InnerMqService;
import com.jmd.ui.frame.control.AddLayerFrame;
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
public class CustomLayerButtonPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 777780616220030369L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();

    @Autowired
    private AddLayerFrame addLayerFrame;

    private final JButton addLayerButton;
    private final JButton removeLayerButton;

    public CustomLayerButtonPanel() {

        this.addLayerButton = new JButton("添加自定义图层 ＋");
        this.addLayerButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.addLayerButton.setFocusable(false);

        this.removeLayerButton = new JButton("删除自定义图层 －");
        this.removeLayerButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.removeLayerButton.setFocusable(false);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(this.addLayerButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.removeLayerButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(this.addLayerButton)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.removeLayerButton)
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {
        this.addLayerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                addLayerFrame.inputNewLayer();
            }
        });
        this.removeLayerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                innerMqService.pub(Topic.REMOVE_ADDED_LAYER, true);
            }
        });
    }

}

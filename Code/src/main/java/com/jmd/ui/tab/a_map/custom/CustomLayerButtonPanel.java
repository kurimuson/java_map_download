package com.jmd.ui.tab.a_map.custom;

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

    @PostConstruct
    private void init() {

        var addLayerButton = new JButton("添加自定义图层 ＋");
        addLayerButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        addLayerButton.setFocusable(false);
        addLayerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                addLayerFrame.inputNewLayer();
            }
        });

        var removeLayerButton = new JButton("删除自定义图层 －");
        removeLayerButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        removeLayerButton.setFocusable(false);
        removeLayerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                innerMqService.pub(Topic.REMOVE_ADDED_LAYER, true);
            }
        });

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(addLayerButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(removeLayerButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(addLayerButton)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(removeLayerButton)
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

}

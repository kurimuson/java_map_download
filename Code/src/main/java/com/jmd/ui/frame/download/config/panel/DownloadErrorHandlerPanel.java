package com.jmd.ui.frame.download.config.panel;

import javax.swing.*;
import java.io.Serial;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.jmd.common.StaticVar;
import org.springframework.stereotype.Component;

@Component
public class DownloadErrorHandlerPanel extends JPanel {

    private final JRadioButton type1RadioButton;
    private final JRadioButton type2RadioButton;
    private final JRadioButton type3RadioButton;

    @Serial
    private static final long serialVersionUID = 824963446218658700L;

    public DownloadErrorHandlerPanel() {

        this.type1RadioButton = new JRadioButton("循环重试下载（失败10次后等待10分钟，继续循环重试）");
        this.type1RadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.type1RadioButton.setFocusable(false);
        this.type1RadioButton.setSelected(true);

        this.type2RadioButton = new JRadioButton("失败5次后弹窗询问是否跳过");
        this.type2RadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.type2RadioButton.setFocusable(false);
        this.type2RadioButton.setSelected(false);

        this.type3RadioButton = new JRadioButton("不处理，直接跳过");
        this.type3RadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.type3RadioButton.setFocusable(false);
        this.type3RadioButton.setSelected(false);

        var btnGroup = new ButtonGroup();
        btnGroup.add(this.type1RadioButton);
        btnGroup.add(this.type2RadioButton);
        btnGroup.add(this.type3RadioButton);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(this.type1RadioButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.type2RadioButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.type3RadioButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(this.type1RadioButton)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.type2RadioButton)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.type3RadioButton)
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    public int getErrorHandlerType() {
        if (this.type1RadioButton.isSelected()) {
            return 1;
        } else if (this.type2RadioButton.isSelected()) {
            return 2;
        } else if (this.type3RadioButton.isSelected()) {
            return 3;
        } else {
            return 1;
        }
    }

}

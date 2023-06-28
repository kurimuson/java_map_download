package com.jmd.ui.tab.c_tile.panel;

import com.jmd.common.StaticVar;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.Serial;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class TileImageTypePanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -3412842231250390141L;

    private final JRadioButton pngRadioButton;
    private final JRadioButton jpgRadioButton;
    private final JRadioButton webpRadioButton;

    private int type = 0;

    public TileImageTypePanel() {

        this.pngRadioButton = new JRadioButton("PNG");
        this.pngRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.pngRadioButton.setFocusable(false);
        this.pngRadioButton.setSelected(true);

        this.jpgRadioButton = new JRadioButton("JPG");
        this.jpgRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.jpgRadioButton.setFocusable(false);

        this.webpRadioButton = new JRadioButton("WEBP");
        this.webpRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.webpRadioButton.setFocusable(false);

        var btnGroup = new ButtonGroup();
        btnGroup.add(this.pngRadioButton);
        btnGroup.add(this.jpgRadioButton);
        btnGroup.add(this.webpRadioButton);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.pngRadioButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.webpRadioButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(this.jpgRadioButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap(9, 9)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(this.pngRadioButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.webpRadioButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.jpgRadioButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {
        this.pngRadioButton.addItemListener((e) -> {
            if (this.pngRadioButton == e.getSource() && this.pngRadioButton.isSelected()) {
                this.type = 0;
            }
        });
        this.webpRadioButton.addItemListener((e) -> {
            if (this.webpRadioButton == e.getSource() && this.webpRadioButton.isSelected()) {
                this.type = 1;
            }
        });
        this.jpgRadioButton.addItemListener((e) -> {
            if (this.jpgRadioButton == e.getSource() && this.jpgRadioButton.isSelected()) {
                this.type = 2;
            }
        });
    }

    public int getImageType() {
        return this.type;
    }

}

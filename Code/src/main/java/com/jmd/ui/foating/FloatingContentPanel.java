package com.jmd.ui.foating;

import com.jmd.common.StaticVar;
import com.jmd.ui.common.IconLabel;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class FloatingContentPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -8670937272253637536L;

    public JLabel progressValueLabel;
    public JLabel downloadSpeedValueLabel;

    public FloatingContentPanel() {

        this.setBackground(new Color(0, 0, 0, 0));

        var logoIconLabel = new IconLabel("assets/icon/map.png");

        this.progressValueLabel = new JLabel("0%");
        this.progressValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        this.progressValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        this.progressValueLabel.setForeground(new Color(192, 192, 192));

        this.downloadSpeedValueLabel = new JLabel("0B/s");
        this.downloadSpeedValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        this.downloadSpeedValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        this.downloadSpeedValueLabel.setForeground(new Color(192, 192, 192));

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(8)
                                .addComponent(logoIconLabel, 26, 26, 26)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(this.downloadSpeedValueLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.progressValueLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addGap(10))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(7)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(logoIconLabel, 26, 26, 26)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.progressValueLabel, 13, 13, 13)
                                                .addGap(0)
                                                .addComponent(this.downloadSpeedValueLabel, 13, 13, 13)))
                                .addGap(7))
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int fieldX = 0;
        int fieldY = 0;
        int fieldWeight = getSize().width;
        int fieldHeight = getSize().height;
        Color bg = new Color(30, 30, 30, 200);
        g.setColor(bg);
        g.fillRoundRect(fieldX, fieldY, fieldWeight, fieldHeight, 20, 20);
        super.paintChildren(g);
    }

}

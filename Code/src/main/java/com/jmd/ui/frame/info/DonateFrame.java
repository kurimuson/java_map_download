package com.jmd.ui.frame.info;

import java.awt.Dimension;
import java.io.Serial;
import java.util.Objects;

import javax.swing.*;

import com.jmd.ui.common.CommonSubFrame;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DonateFrame extends CommonSubFrame {

    @Serial
    private static final long serialVersionUID = -7739920320485676764L;

    @PostConstruct
    private void init() {

        this.getContentPane().setLayout(null);

        var panel = new JPanel();
        panel.setBounds(0, 0, 631, 450);
        panel.setLayout(null);

        var alipayImageLabel = new JLabel("");
        alipayImageLabel.setBounds(0, 0, 300, 450);
        alipayImageLabel.setIcon(new ImageIcon(Objects.requireNonNull(DonateFrame.class.getResource("/com/jmd/assets/donate/alipay.jpg"))));
        panel.add(alipayImageLabel);

        var wechatImageLabel = new JLabel("");
        wechatImageLabel.setIcon(new ImageIcon(Objects.requireNonNull(DonateFrame.class.getResource("/com/jmd/assets/donate/wechat.jpg"))));
        wechatImageLabel.setBounds(300, 0, 331, 450);
        panel.add(wechatImageLabel);
        getContentPane().add(panel);

        this.setTitle("捐赠开发者（非强制，不影响软件使用）");
        this.setSize(new Dimension(635, 477));
        this.setVisible(false);
        this.setResizable(false);

    }

}

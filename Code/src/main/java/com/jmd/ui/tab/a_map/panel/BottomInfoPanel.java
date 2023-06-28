package com.jmd.ui.tab.a_map.panel;

import javax.swing.JPanel;

import com.jmd.browser.core.ChromiumEmbeddedCore;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;

import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.io.Serial;

@Component
public class BottomInfoPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -5223551680329511378L;

    public BottomInfoPanel() {

        var version = " JetBrains Runtime: " +
                System.getProperty("java.vm.version") +
                ", " +
                "Chromium Embedded Framework (CEF), " +
                "ChromeVersion: " +
                ChromiumEmbeddedCore.Companion.getInstance().getVersion();

        this.setLayout(new BorderLayout(0, 0));
        JLabel leftLabel = new JLabel(version);
        leftLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        this.add(leftLabel, BorderLayout.WEST);

    }

}

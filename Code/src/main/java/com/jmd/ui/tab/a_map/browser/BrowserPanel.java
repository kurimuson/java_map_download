package com.jmd.ui.tab.a_map.browser;

import java.awt.*;
import java.io.Serial;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jmd.browser.BrowserType;
import com.jmd.ui.common.CommonDialog;
import com.jmd.ui.tab.a_map.bottom.BottomInfoPanel;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.browser.BrowserEngine;
import com.jmd.common.StaticVar;

@Component
public class BrowserPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 6855207015589162698L;

    @Autowired
    private BottomInfoPanel bottomInfoPanel;
    @Autowired
    private BrowserEngine browserEngine;

    private JPanel browserPanel;
    private JPanel devToolPanel;
    @Getter
    private boolean devToolOpen = false;
    private JSplitPane splitPane;

    @PostConstruct
    private void init() {

        this.setLayout(new BorderLayout());

        this.splitPane = new JSplitPane();
        this.splitPane.setDividerSize(0);
        this.splitPane.setOneTouchExpandable(false);
        this.splitPane.setContinuousLayout(false);
        this.splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.add(this.splitPane, BorderLayout.CENTER);

        // 浏览器
        this.browserPanel = new JPanel();
        this.browserPanel.setLayout(new BorderLayout());
        this.splitPane.setLeftComponent(this.browserPanel);

        // 开发者工具
        this.devToolPanel = new JPanel();
        this.devToolPanel.setLayout(new BorderLayout());
        this.splitPane.setRightComponent(null);

        var label = new JLabel("WebView初始化");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.browserPanel.add(label, BorderLayout.CENTER);

        this.createBrowser();

    }

    private void createBrowser() {
        this.browserEngine.init((browser) -> {
            SwingUtilities.invokeLater(() -> {
                this.browserPanel.removeAll();
                this.browserPanel.add(browser.getBrowserContainer(), BorderLayout.CENTER);
                this.bottomInfoPanel.getContentLabel().setText(this.browserEngine.getVersion());
            });
        });
    }

    public void toggleDevTools() {
        if (this.browserEngine.getBrowserType() == BrowserType.CHROMIUM_EMBEDDED_CEF_BROWSER) {
            if (this.devToolOpen) {
                this.closeDevTools();
                this.devToolOpen = false;
            } else {
                this.openDevTools();
                this.devToolOpen = true;
            }
        } else {
            CommonDialog.alert(null, "当前内核不支持");
        }
    }

    private void openDevTools() {
        SwingUtilities.invokeLater(() -> {
            this.devToolPanel.add(this.browserEngine.getBrowser().getDevToolsContainer(), BorderLayout.CENTER);
            this.splitPane.setDividerSize(5);
            this.splitPane.setRightComponent(this.devToolPanel);
            this.splitPane.setContinuousLayout(true);
            this.splitPane.setDividerLocation(this.getSize().width - 500);
        });
    }

    private void closeDevTools() {
        SwingUtilities.invokeLater(() -> {
            this.devToolPanel.removeAll();
            this.splitPane.remove(this.devToolPanel);
            this.splitPane.setDividerSize(0);
            this.splitPane.setRightComponent(null);
            this.splitPane.setContinuousLayout(false);
            this.splitPane.setDividerLocation(this.getSize().width);
        });
    }

}

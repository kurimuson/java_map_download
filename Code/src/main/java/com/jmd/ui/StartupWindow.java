package com.jmd.ui;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.jmd.common.StaticVar;

import com.jmd.ui.common.IconLabel;
import lombok.Getter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.Serial;
import javax.swing.border.LineBorder;

public class StartupWindow extends JWindow {

    @Serial
    private static final long serialVersionUID = -8349753659929843051L;

    private static volatile StartupWindow instance;

    @Getter
    private final JProgressBar progressBar;
    @Getter
    private final JLabel progressLabel;
    @Getter
    private final JLabel beanNameLabel;

    public StartupWindow() {

        this.getContentPane().setLayout(null);

        var backgroundPanel = new JPanel();
        var borderColor = new Color(112, 112, 112);
        backgroundPanel.setBorder(new LineBorder(borderColor));
        backgroundPanel.setBounds(0, 0, 400, 301);
        backgroundPanel.setLayout(null);
        getContentPane().add(backgroundPanel);

        var titleLabel = new JLabel("地图下载器");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        titleLabel.setBounds(251, 10, 139, 21);
        titleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        backgroundPanel.add(titleLabel);

        var runtimeLabel = new JLabel("Java Swing with JetBrains Runtime");
        runtimeLabel.setForeground(Color.BLACK);
        runtimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        runtimeLabel.setBounds(120, 272, 270, 21);
        runtimeLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        backgroundPanel.add(runtimeLabel);

        this.progressLabel = new JLabel("正在加载");
        this.progressLabel.setForeground(Color.BLACK);
        this.progressLabel.setBounds(10, 273, 100, 18);
        this.progressLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        backgroundPanel.add(this.progressLabel);

        var backgroundLabel = new IconLabel("assets/img/load-background-hi.png");
        backgroundLabel.setBounds(1, 1, 398, 298);
        backgroundPanel.add(backgroundLabel);

        var beanNamePanel = new JPanel();
        beanNamePanel.setBorder(new LineBorder(borderColor));
        beanNamePanel.setBounds(0, 300, 400, 26);
        beanNamePanel.setLayout(null);
        getContentPane().add(beanNamePanel);

        this.beanNameLabel = new JLabel("org.springframework.boot");
        this.beanNameLabel.setBounds(10, 0, 380, 25);
        this.beanNameLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        beanNamePanel.add(this.beanNameLabel);

        var progressBarPanel = new JPanel();
        progressBarPanel.setBorder(new LineBorder(borderColor));
        progressBarPanel.setBounds(0, 325, 400, 15);
        getContentPane().add(progressBarPanel);
        progressBarPanel.setLayout(new BorderLayout());

        this.progressBar = new JProgressBar();
        progressBarPanel.add(this.progressBar, BorderLayout.CENTER);

        this.setSize(400, 340);
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                instance = null;
            }
        });

    }

    public void close() {
        SwingUtilities.invokeLater(() -> {
            this.dispose();
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
    }

    public static StartupWindow getInstance() {
        if (instance == null) {
            synchronized (StartupWindow.class) {
                if (instance == null) {
                    instance = new StartupWindow();
                }
            }
        }
        return instance;
    }

}

package com.jmd.ui.tab.b_download.panel.cpu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.Serial;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class CPUPercentageLinePanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -910619749930001376L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    private final double[] systemCpuUsage = new double[60];
    private final double[] processCpuUsage = new double[60];

    public CPUPercentageLinePanel() {
        this.setForeground(Color.CYAN);
        this.setBorder(new LineBorder(new Color(128, 128, 128)));
        this.setBounds(260, 17, 120, 100);
        this.setLayout(null);
    }

    @PostConstruct
    private void init() {
        try {
            this.subInnerMqMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    protected void destroy() {
        this.innerMqService.destroyClient(this.client);
    }

    private void subInnerMqMessage() throws Exception {
        this.client = this.innerMqService.createClient();
        this.client.sub(Topic.CPU_PERCENTAGE_DRAW_SYSTEM, this::drawCpuSystemUsage);
        this.client.sub(Topic.CPU_PERCENTAGE_DRAW_PROCESS, this::drawCpuProcessUsage);
        this.client.sub(Topic.CPU_PERCENTAGE_CLEAR, (res) -> clear());
    }

    private void drawCpuSystemUsage(double currentSystemCpuUsage) {
        for (int i = 0; i < 59; i++) {
            systemCpuUsage[i] = systemCpuUsage[i + 1];
        }
        systemCpuUsage[59] = currentSystemCpuUsage;
        SwingUtilities.invokeLater(this::repaint);
    }

    private void drawCpuProcessUsage(double currentProcessCpuUsage) {
        for (int i = 0; i < 59; i++) {
            processCpuUsage[i] = processCpuUsage[i + 1];
        }
        processCpuUsage[59] = currentProcessCpuUsage;
        SwingUtilities.invokeLater(this::repaint);
    }

    private void clear() {
        for (int i = 0; i < 59; i++) {
            systemCpuUsage[i] = 0.0;
            processCpuUsage[i] = 0.0;
        }
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
        var x = 0;
        for (var i = 0; i < 59; i++) {
            var x1 = x;
            var x2 = x + 2;
            var height = 120;
            var ys1 = (int) Math.round(height * (1 - systemCpuUsage[i]));
            var yp1 = (int) Math.round(height * (1 - processCpuUsage[i]));
            var ys2 = (int) Math.round(height * (1 - systemCpuUsage[i + 1]));
            var yp2 = (int) Math.round(height * (1 - processCpuUsage[i + 1]));
            g.setColor(new Color(51, 102, 204));
            g.drawLine(x1, ys1, x2, ys2);
            g.setColor(new Color(255, 102, 0));
            g.drawLine(x1, yp1, x2, yp2);
            x = x + 2;
        }
    }

}

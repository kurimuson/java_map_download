package com.jmd.ui.tab.d_syslog;

import javax.swing.JPanel;

import com.jmd.ApplicationStore;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;

import java.awt.BorderLayout;
import java.io.Serial;
import javax.swing.JScrollPane;

@Component
public class SystemLogPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 1300659845262270172L;

    private final JScrollPane scrollPane;

    public SystemLogPanel() {

        this.setLayout(new BorderLayout(0, 0));

        var panel = new JPanel();
        this.add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        this.scrollPane = new JScrollPane();
        panel.add(this.scrollPane, BorderLayout.CENTER);

    }

    @PostConstruct
    private void init() {
        var textArea = ApplicationStore.consoleTextArea;
        textArea.setEditable(false);
        textArea.setFont(StaticVar.FONT_YaHeiConsolas_13);
        this.scrollPane.setViewportView(textArea);
    }

}

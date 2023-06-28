package com.jmd.ui.tab.b_download.panel;

import com.jmd.common.StaticVar;
import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.text.SimpleDateFormat;

@Component
public class TaskLogPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -1939864312267681290L;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    private final JTextArea textArea;

    public TaskLogPanel() {

        this.setLayout(new BorderLayout());

        var logScrollPane = new JScrollPane();
        this.add(logScrollPane, BorderLayout.CENTER);

        this.textArea = new JTextArea();
        this.textArea.setEditable(false);
        this.textArea.setFocusable(false);
        this.textArea.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        logScrollPane.setViewportView(this.textArea);

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
        this.client.sub(Topic.DOWNLOAD_CONSOLE_LOG, this::consoleLog);
        this.client.sub(Topic.DOWNLOAD_CONSOLE_CLEAR, (res) -> {
            clearConsole();
        });
    }

    public void consoleLog(String log) {
        var time = "[" + this.sdf.format(System.currentTimeMillis()) + "]";
        var content = time + " " + log;
        SwingUtilities.invokeLater(() -> {
            this.textArea.append(content + "\n");
            this.textArea.setCaretPosition(this.textArea.getText().length());
        });
    }

    public void clearConsole() {
        SwingUtilities.invokeLater(() -> {
            this.textArea.setText("");
        });
    }

}

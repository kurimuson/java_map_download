package com.jmd.ui.frame.download.config.panel;

import com.jmd.ApplicationSetting;
import com.jmd.common.StaticVar;
import com.jmd.rx.Topic;
import com.jmd.rx.service.InnerMqService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.Serial;

@Component
public class DownloadPathSelectorPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 5002417246102831170L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();

    private final JTextArea textArea;
    private final JButton pathSelectorButton;

    private String lastDirPath;

    @Getter
    private boolean hasSelected = false;
    @Getter
    private File selectedDirPath;

    public DownloadPathSelectorPanel() {

        var scrollPane = new JScrollPane();

        this.textArea = new JTextArea();
        this.textArea.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.textArea.setEditable(false);
        this.textArea.setFocusable(false);
        scrollPane.setViewportView(this.textArea);

        this.pathSelectorButton = new JButton("选择路径");
        this.pathSelectorButton.setFocusable(false);
        this.pathSelectorButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.pathSelectorButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.pathSelectorButton)
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {

        var ldp = ApplicationSetting.getSetting().getLastDirPath();
        if (ldp != null) {
            var ldp_dir = new File(ldp);
            if (ldp_dir.isDirectory()) {
                this.hasSelected = true;
                this.lastDirPath = ldp;
                this.selectedDirPath = ldp_dir;
            }
        }

        if (this.lastDirPath != null) {
            this.textArea.setText(this.lastDirPath);
        }

        this.pathSelectorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    var chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (lastDirPath != null && !"".equals(lastDirPath)) {
                        chooser.setCurrentDirectory(new File(lastDirPath));
                    }
                    chooser.setDialogTitle("选择保存路径");
                    chooser.setApproveButtonText("确定");
                    chooser.showOpenDialog(null);
                    var file = chooser.getSelectedFile();
                    if (file == null) {
                        hasSelected = false;
                    } else {
                        hasSelected = true;
                        // 设置路径
                        lastDirPath = file.getAbsolutePath();
                        selectedDirPath = file;
                        textArea.setText(file.getAbsolutePath());
                        // 保存配置
                        var setting = ApplicationSetting.getSetting();
                        setting.setLastDirPath(lastDirPath);
                        ApplicationSetting.save(setting);
                    }
                    innerMqService.pub(Topic.DOWNLOAD_CONFIG_FRAME_PATH_SELECTED, hasSelected);
                }
            }
        });

    }

}

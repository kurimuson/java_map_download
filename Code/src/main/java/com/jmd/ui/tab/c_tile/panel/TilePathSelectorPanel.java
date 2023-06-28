package com.jmd.ui.tab.c_tile.panel;

import com.jmd.Application;
import com.jmd.ApplicationSetting;
import com.jmd.callback.TileViewSubmitCallback;
import com.jmd.ui.common.CommonDialog;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.Serial;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class TilePathSelectorPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -3146886045134527901L;

    private TileViewSubmitCallback callback;
    private String selectedPath;
    private String lastDirPath = this.getCachedLastDirPath();

    private final JButton pathSelectorButton;
    private final JButton submitButton;
    private final JTextArea pathValueTextArea;
    private final JComboBox<String> pathStyleDefaultComboBox;

    public TilePathSelectorPanel() {

        this.pathSelectorButton = new JButton("选择路径");
        this.pathSelectorButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.pathSelectorButton.setFocusable(false);

        var pathValuePanel = new JScrollPane();

        this.pathValueTextArea = new JTextArea();
        this.pathValueTextArea.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.pathValueTextArea.setEditable(false);
        this.pathValueTextArea.setFocusable(false);
        pathValuePanel.setViewportView(this.pathValueTextArea);

        this.submitButton = new JButton("加载瓦片");
        this.submitButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.submitButton.setFocusable(false);
        this.submitButton.setEnabled(false);

        this.pathStyleDefaultComboBox = new JComboBox<>();
        this.pathStyleDefaultComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.pathStyleDefaultComboBox.setFocusable(false);
        this.pathStyleDefaultComboBox.setModel(new DefaultComboBoxModel<>(StaticVar.PATH_STYLE));
        this.pathStyleDefaultComboBox.setSelectedIndex(1);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.pathSelectorButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(pathValuePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.submitButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.pathStyleDefaultComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
                                        .addComponent(this.pathSelectorButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pathValuePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(this.submitButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.pathStyleDefaultComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {
        this.pathSelectorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    var chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (lastDirPath != null && !"".equals(lastDirPath)) {
                        chooser.setCurrentDirectory(new File(lastDirPath));
                    }
                    chooser.setDialogTitle("选择瓦片路径");
                    chooser.setApproveButtonText("确定");
                    chooser.showOpenDialog(null);
                    var file = chooser.getSelectedFile();
                    if (file != null) {
                        selectedPath = file.getAbsolutePath();
                        lastDirPath = file.getAbsolutePath();
                        pathValueTextArea.setText(file.getAbsolutePath());
                        submitButton.setEnabled(true);
                    }
                }
            }
        });
        this.submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1 && submitButton.isEnabled() && callback != null && Application.isStartComplete) {
                    callback.execute();
                }
            }
        });
    }

    private String getCachedLastDirPath() {
        var ldp = ApplicationSetting.getSetting().getLastDirPath();
        if (ldp != null) {
            var ldp_dir = new File(ldp);
            if (ldp_dir.isDirectory()) {
                return ldp;
            }
        }
        return null;
    }

    public String getTilePath() {
        return this.selectedPath;
    }

    public String getTilePathStyle() {
        return (String) pathStyleDefaultComboBox.getSelectedItem();
    }

    public void setCallback(TileViewSubmitCallback callback) {
        this.callback = callback;
    }

}

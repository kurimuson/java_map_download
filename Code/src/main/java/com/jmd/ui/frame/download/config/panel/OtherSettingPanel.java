package com.jmd.ui.frame.download.config.panel;

import com.jmd.common.StaticVar;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.Serial;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class OtherSettingPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 5896768433169791370L;

    private JComboBox<String> imgTypeComboBox;
    private JComboBox<String> pathStyleDefaultComboBox;
    private JCheckBox isCoverCheckBox;
    private JCheckBox mergeTileCheckBox;
    private JLabel mergeTypeLabel;
    private JComboBox<String> mergeTypeComboBox;
    private JTextArea mergeTipTextArea;

//    public OtherSettingPanel() {
//        init();
//    }

    @PostConstruct
    private void init() {

        var imgTypeLabel = new JLabel("图片格式：");
        imgTypeLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        this.imgTypeComboBox = new JComboBox<>();
        this.imgTypeComboBox.setFocusable(false);
        this.imgTypeComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.imgTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"PNG", "WEBP", "JPG-低", "JPG-中", "JPG-高"}));
        this.imgTypeComboBox.setSelectedIndex(0);

        var pathStyleLabel = new JLabel("命名风格：");
        pathStyleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        var pathStyleRadioButton1 = new JRadioButton("预设");
        pathStyleRadioButton1.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        pathStyleRadioButton1.setFocusable(false);
        pathStyleRadioButton1.setSelected(true);

        var pathStyleBtnGroup = new ButtonGroup();
        pathStyleBtnGroup.add(pathStyleRadioButton1);

        this.pathStyleDefaultComboBox = new JComboBox<String>();
        this.pathStyleDefaultComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.pathStyleDefaultComboBox.setFocusable(false);
        this.pathStyleDefaultComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "/{z}/{x}/{y}/x={x}&y={y}&z={z}.[image]",
                        "/{z}/{x}/{y}/tile.[image]",
                        "/{z}/{y}/{x}.[image]",
                        "/{z}/{x}/{y}.[image]"
                })
        );
        this.pathStyleDefaultComboBox.setSelectedIndex(1);

        this.isCoverCheckBox = new JCheckBox("覆盖已下载的瓦片");
        this.isCoverCheckBox.setSelected(true);
        this.isCoverCheckBox.setFocusable(false);
        this.isCoverCheckBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        this.mergeTileCheckBox = new JCheckBox("合并下载的瓦片");
        this.mergeTileCheckBox.setFocusable(false);
        this.mergeTileCheckBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.mergeTileCheckBox.addActionListener((e) -> {
            var f = this.mergeTileCheckBox.isSelected();
            this.mergeTypeLabel.setVisible(f);
            this.mergeTypeComboBox.setVisible(f);
            this.mergeTipTextArea.setVisible(f);
        });

        this.mergeTypeLabel = new JLabel("合并格式：");
        this.mergeTypeLabel.setVisible(false);
        this.mergeTypeLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        this.mergeTypeComboBox = new JComboBox<>();
        this.mergeTypeComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.mergeTypeComboBox.setVisible(false);
        this.mergeTypeComboBox.setFocusable(false);
        this.mergeTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"自动", "WEBP", "JPG", "PNG"}));
        this.mergeTypeComboBox.setSelectedIndex(0);

        this.mergeTipTextArea = new JTextArea("" +
                "基于已下载的瓦片进行拼接合并，" +
                "WEBP与PNG可保留透明度，JPG则使用黑色代替透明位置，但会获得更小的体积。\n" +
                "自动：默认导出为WEBP，超过16383*16383导出为JPG，超过65535*65535导出为PNG。" +
                "");
        this.mergeTipTextArea.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.mergeTipTextArea.setVisible(false);
        this.mergeTipTextArea.setLineWrap(true);
        this.mergeTipTextArea.setEditable(false);
        this.mergeTipTextArea.setFocusable(false);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(imgTypeLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(imgTypeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(pathStyleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(pathStyleRadioButton1))
                                        .addComponent(pathStyleDefaultComboBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(isCoverCheckBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(mergeTileCheckBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(mergeTypeLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(mergeTypeComboBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(mergeTipTextArea, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(imgTypeLabel)
                                        .addComponent(imgTypeComboBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(pathStyleLabel)
                                        .addComponent(pathStyleRadioButton1))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(pathStyleDefaultComboBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(isCoverCheckBox)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(mergeTileCheckBox)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mergeTypeLabel)
                                        .addComponent(mergeTypeComboBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(mergeTipTextArea, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    public String getPathStyle() {
        return (String) pathStyleDefaultComboBox.getSelectedItem();
    }

    public int getImgType() {
        return this.imgTypeComboBox.getSelectedIndex();
    }

    public boolean isCoverExist() {
        return isCoverCheckBox.isSelected();
    }

    public boolean isMergeTile() {
        return mergeTileCheckBox.isSelected();
    }

    public int getMergeType() {
        return this.mergeTypeComboBox.getSelectedIndex();
    }

}

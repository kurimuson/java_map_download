package com.jmd.ui.frame.control;

import com.jmd.ApplicationSetting;
import com.jmd.common.StaticVar;
import com.jmd.entity.setting.AddedLayerSetting;
import com.jmd.rx.Topic;
import com.jmd.rx.service.InnerMqService;
import com.jmd.ui.common.CommonDialog;
import com.jmd.ui.common.CommonSubFrame;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class AddLayerFrame extends CommonSubFrame {

    @Serial
    private static final long serialVersionUID = 6036357857173849529L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();

    private JTextField nameInputTextField;
    private JTextArea urlInputTextArea;
    private String type = "wgs84";

//	public AddLayerFrame() {
//		init();
//	}

    @PostConstruct
    private void init() {

        var nameInputLabel = new JLabel("标题：");
        nameInputLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        this.nameInputTextField = new JTextField();

        JLabel typeSelectLabel = new JLabel("坐标类型：");
        typeSelectLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        JRadioButton typeWgs84RadioButton = new JRadioButton("wgs84");
        typeWgs84RadioButton.setSelected(true);
        typeWgs84RadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        typeWgs84RadioButton.addItemListener((e) -> {
            if (typeWgs84RadioButton == e.getSource() && typeWgs84RadioButton.isSelected()) {
                this.type = "wgs84";
            }
        });

        JRadioButton typeGcj02RadioButton = new JRadioButton("gcj02");
        typeGcj02RadioButton.setSelected(false);
        typeGcj02RadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        typeGcj02RadioButton.addItemListener((e) -> {
            if (typeGcj02RadioButton == e.getSource() && typeGcj02RadioButton.isSelected()) {
                this.type = "gcj02";
            }
        });

        var btnGroup = new ButtonGroup();
        btnGroup.add(typeWgs84RadioButton);
        btnGroup.add(typeGcj02RadioButton);

        var urlInputLabel = new JLabel("地址：（请严格按照{x}{y}{z}的格式填写地址）");
        urlInputLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        var urlInputScrollPane = new JScrollPane();
        this.urlInputTextArea = new JTextArea();
        this.urlInputTextArea.setLineWrap(true);
        urlInputScrollPane.setViewportView(urlInputTextArea);

        var okButton = new JButton("确定");
        okButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        okButton.setFocusable(false);
        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                saveNewLayer();
            }
        });

        var groupLayout = new GroupLayout(this.getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(urlInputLabel, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                                        .addComponent(nameInputTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                                        .addComponent(typeSelectLabel, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(typeWgs84RadioButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addComponent(typeGcj02RadioButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(nameInputLabel, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                                        .addComponent(urlInputScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                                        .addComponent(okButton, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(nameInputLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(nameInputTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(typeSelectLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(typeWgs84RadioButton)
                                        .addComponent(typeGcj02RadioButton))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(urlInputLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(urlInputScrollPane, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(okButton)
                                .addContainerGap())
        );
        this.getContentPane().setLayout(groupLayout);

        this.setTitle("添加自定义图层");
        this.setSize(new Dimension(360, 300));
        this.setVisible(false);
        this.setResizable(false);

    }

    @PreDestroy
    protected void destroy() {
        super.destroy();
    }

    public void inputNewLayer() {
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }

    private void saveNewLayer() {
        String name = this.nameInputTextField.getText();
        String url = this.urlInputTextArea.getText();
        // 判断非空
        if (name == null || "".equals(name)) {
            CommonDialog.alert(null, "标题不能为空");
            return;
        }
        if (url == null || "".equals(url)) {
            CommonDialog.alert(null, "地址不能为空");
            return;
        }
        // 检测重复
        var addedLayers = ApplicationSetting.getSetting().getAddedLayers();
        for (var layer : addedLayers) {
            if (name.equals(layer.getName())) {
                CommonDialog.alert(null, "图层名不能重复");
                return;
            }
        }
        // 生成对象
        var result = new AddedLayerSetting();
        result.setName(name);
        result.setUrl(url);
        result.setType(this.type);
        // 保存设置
        ApplicationSetting.getSetting().getAddedLayers().add(result);
        ApplicationSetting.save();
        // 发布
        this.innerMqService.pub(Topic.ADD_NEW_LAYER, result);
        // 关闭窗口
        SwingUtilities.invokeLater(() -> {
            this.nameInputTextField.setText(null);
            this.urlInputTextArea.setText(null);
            this.setVisible(false);
        });
    }

}

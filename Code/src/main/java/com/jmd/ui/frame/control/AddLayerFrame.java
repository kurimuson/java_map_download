package com.jmd.ui.frame.control;

import com.jmd.ApplicationSetting;
import com.jmd.common.StaticVar;
import com.jmd.model.setting.AddedLayerSetting;
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

    private final JTextField nameInputTextField;
    private final JRadioButton typeWgs84RadioButton;
    private final JRadioButton typeGcj02RadioButton;
    private final JRadioButton proxyCloseRadioButton;
    private final JRadioButton proxyOpenRadioButton;
    private final JTextArea urlInputTextArea;
    private final JButton okButton;
    private String type = "wgs84";
    private boolean proxy = false;

    public AddLayerFrame() {

        var nameInputLabel = new JLabel("标题：");
        nameInputLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        this.nameInputTextField = new JTextField();

        var typeSelectLabel = new JLabel("坐标类型：");
        typeSelectLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        this.typeWgs84RadioButton = new JRadioButton("wgs84");
        this.typeWgs84RadioButton.setSelected(true);
        this.typeWgs84RadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        this.typeGcj02RadioButton = new JRadioButton("gcj02");
        this.typeGcj02RadioButton.setSelected(false);
        this.typeGcj02RadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        var btnGroup1 = new ButtonGroup();
        btnGroup1.add(this.typeWgs84RadioButton);
        btnGroup1.add(this.typeGcj02RadioButton);

        var proxyLabel = new JLabel("瓦片资源访问方式：");
        proxyLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        this.proxyCloseRadioButton = new JRadioButton("浏览器页面直接访问");
        this.proxyCloseRadioButton.setSelected(true);
        this.proxyCloseRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        this.proxyOpenRadioButton = new JRadioButton("通过okhttp代理访问");
        this.proxyOpenRadioButton.setSelected(false);
        this.proxyOpenRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        var btnGroup2 = new ButtonGroup();
        btnGroup2.add(this.proxyCloseRadioButton);
        btnGroup2.add(this.proxyOpenRadioButton);

        var urlInputLabel = new JLabel("地址：（请严格按照{x}{y}{z}的格式填写地址）");
        urlInputLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

        var urlInputScrollPane = new JScrollPane();
        this.urlInputTextArea = new JTextArea();
        this.urlInputTextArea.setLineWrap(true);
        urlInputScrollPane.setViewportView(this.urlInputTextArea);

        this.okButton = new JButton("确定");
        this.okButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.okButton.setFocusable(false);

        var groupLayout = new GroupLayout(this.getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                                        .addComponent(nameInputLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                        .addComponent(nameInputTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                        .addComponent(typeSelectLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                        .addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
                                                                .addComponent(typeWgs84RadioButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(typeGcj02RadioButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(proxyLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                        .addComponent(proxyCloseRadioButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                        .addComponent(proxyOpenRadioButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                        .addComponent(urlInputLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                        .addComponent(urlInputScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                                        .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap())))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(nameInputLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(nameInputTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(typeSelectLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(typeWgs84RadioButton)
                                        .addComponent(typeGcj02RadioButton))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(proxyLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(proxyCloseRadioButton)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(proxyOpenRadioButton)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(urlInputLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(urlInputScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(okButton)
                                .addContainerGap())
        );
        this.getContentPane().setLayout(groupLayout);

        this.setTitle("添加自定义图层");
        this.setSize(new Dimension(400, 400));
        this.setVisible(false);
        this.setResizable(false);

    }

    @PostConstruct
    private void init() {
        this.typeWgs84RadioButton.addItemListener((e) -> {
            if (this.typeWgs84RadioButton == e.getSource() && this.typeWgs84RadioButton.isSelected()) {
                this.type = "wgs84";
            }
        });
        this.typeGcj02RadioButton.addItemListener((e) -> {
            if (this.typeGcj02RadioButton == e.getSource() && this.typeGcj02RadioButton.isSelected()) {
                this.type = "gcj02";
            }
        });
        this.proxyCloseRadioButton.addItemListener((e) -> {
            if (this.proxyCloseRadioButton == e.getSource() && this.proxyCloseRadioButton.isSelected()) {
                this.proxy = false;
            }
        });
        this.proxyOpenRadioButton.addItemListener((e) -> {
            if (this.proxyOpenRadioButton == e.getSource() && this.proxyOpenRadioButton.isSelected()) {
                this.proxy = true;
            }
        });
        this.okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                saveNewLayer();
            }
        });
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
        result.setProxy(this.proxy);
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

package com.jmd.ui.frame.info;

import java.awt.*;
import javax.swing.*;

import com.jmd.ui.common.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.util.CommonUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serial;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.border.EtchedBorder;

@Component
public class AboutFrame extends CommonSubFrame {

    @Serial
    private static final long serialVersionUID = -5612514860347124476L;

    @Value("${setting.version.jdk}")
    private String jdkVersion;
    @Value("${setting.version.jcef}")
    private String jcefVersion;
    @Value("${setting.version.openlayers}")
    private String openlayersVersion;
    @Value("${setting.version.angular}")
    private String angularVersion;
    @Value("${setting.version.opencv}")
    private String opencvVersion;
    @Value("${setting.build.date}")
    private String buildDate;
    @Value("${setting.git.address}")
    private String gitAddress;

    private final JLabel jdkTextLabel;
    private final JLabel jcefTextLabel;
    private final JLabel openlayersTextLabel;
    private final JLabel angularTextLabel;
    private final JLabel opencvTextLabel;
    private final JLabel gitTextLabel;
    private final JTextArea tipTextArea;
    private final JButton gitCopyButton;
    private final JButton gitOpenButton;

    public AboutFrame() {

        var springBootTextArea = new JTextArea();
        this.getContentPane().add(springBootTextArea, BorderLayout.NORTH);
        springBootTextArea.setFont(StaticVar.FONT_YaHeiConsolas_13);
        springBootTextArea.setText(""
                + "  .   ____          _            __ _ _\n"
                + " /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\\n"
                + "( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\\n"
                + " \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )\n"
                + "  '  |____| .__|_| |_|_| |_\\__, | / / / /\n"
                + " =========|_|==============|___/=/_/_/_/\n"
                + " :: Spring Boot ::        (" + SpringBootVersion.getVersion() + ")" + "");
        springBootTextArea.setEditable(false);

        var panel = new JPanel();
        panel.setLayout(null);
        this.getContentPane().add(panel, BorderLayout.CENTER);

        var jdkIconLabel = new IconLabel("assets/icon/jetbrains.png");
        jdkIconLabel.setBounds(15, 10, 30, 30);
        panel.add(jdkIconLabel);

        this.jdkTextLabel = new JLabel();
        this.jdkTextLabel.setBounds(55, 10, 166, 30);
        this.jdkTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        panel.add(this.jdkTextLabel);

        var jcefIconLabel = new IconLabel("assets/icon/cef.png");
        jcefIconLabel.setBounds(238, 10, 40, 30);
        panel.add(jcefIconLabel);

        this.jcefTextLabel = new JLabel();
        this.jcefTextLabel.setBounds(288, 10, 156, 30);
        this.jcefTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        panel.add(this.jcefTextLabel);

        var openlayersIconLabel = new IconLabel("assets/icon/openlayers.png");
        openlayersIconLabel.setBounds(15, 46, 30, 30);
        panel.add(openlayersIconLabel);

        this.openlayersTextLabel = new JLabel();
        this.openlayersTextLabel.setBounds(55, 46, 166, 30);
        this.openlayersTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        panel.add(this.openlayersTextLabel);

        var angularIconLabel = new IconLabel("assets/icon/angular.png");
        angularIconLabel.setBounds(238, 46, 30, 30);
        panel.add(angularIconLabel);

        this.angularTextLabel = new JLabel();
        this.angularTextLabel.setBounds(278, 46, 166, 30);
        this.angularTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        panel.add(this.angularTextLabel);

        var opencvIconLabel = new IconLabel("assets/icon/opencv.png");
        opencvIconLabel.setBounds(15, 82, 30, 30);
        panel.add(opencvIconLabel);

        this.opencvTextLabel = new JLabel();
        this.opencvTextLabel.setBounds(55, 82, 166, 30);
        this.opencvTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        panel.add(this.opencvTextLabel);

        var gitIconLabel = new IconLabel("assets/icon/git.png");
        gitIconLabel.setBounds(15, 118, 30, 30);
        panel.add(gitIconLabel);

        this.gitTextLabel = new JLabel();
        this.gitTextLabel.setBounds(55, 118, 389, 30);
        this.gitTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        panel.add(this.gitTextLabel);

        this.gitCopyButton = new JButton("复制git地址");
        this.gitCopyButton.setBounds(10, 158, 217, 29);
        this.gitCopyButton.setFocusable(false);
        this.gitCopyButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        panel.add(this.gitCopyButton);

        this.gitOpenButton = new JButton("打开git");
        this.gitOpenButton.setBounds(233, 158, 211, 29);
        this.gitOpenButton.setFocusable(false);
        this.gitOpenButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        panel.add(this.gitOpenButton);

        this.tipTextArea = new JTextArea();
        this.tipTextArea.setBounds(10, 193, 434, 78);
        this.tipTextArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        this.tipTextArea.setLineWrap(true);
        this.tipTextArea.setEditable(false);
        panel.add(tipTextArea);

        this.setTitle("关于地图下载器");
        this.setSize(new Dimension(470, 450));
        this.setVisible(false);
        this.setResizable(false);

    }

    @PostConstruct
    private void init() {
        this.jdkTextLabel.setText(this.jdkVersion);
        this.jcefTextLabel.setText("JCEF " + this.jcefVersion);
        this.openlayersTextLabel.setText("OpenLayers " + this.opencvVersion);
        this.angularTextLabel.setText("Angular " + this.angularVersion);
        this.opencvTextLabel.setText("OpenCV " + this.opencvVersion);
        this.tipTextArea.setText("Build日期：" + this.buildDate);
        this.gitTextLabel.setText(this.gitAddress);
        this.gitCopyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    CommonUtils.setClipboardText(gitAddress);
                    CommonDialog.alert(null, "已复制到剪贴板");
                }
            }
        });
        this.gitOpenButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    try {
                        var desktop = Desktop.getDesktop();
                        desktop.browse(new URI(gitAddress));
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

}

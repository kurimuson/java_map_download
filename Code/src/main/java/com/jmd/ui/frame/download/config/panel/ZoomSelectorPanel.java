package com.jmd.ui.frame.download.config.panel;

import com.jmd.common.StaticVar;
import com.jmd.rx.Topic;
import com.jmd.rx.service.InnerMqService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

@Component
public class ZoomSelectorPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -8981815091348698167L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();

    private final ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
    private JCheckBox selectAllCheckBox;

    @Getter
    private boolean hasSelected = false;

    @PostConstruct
    private void init() {

        this.setLayout(new BorderLayout(0, 0));

        var scrollPane = new JScrollPane();
        this.add(scrollPane, BorderLayout.CENTER);

        var container = new JPanel();
        scrollPane.setViewportView(container);

        GridBagLayout gbl_layerPanelContent = new GridBagLayout();
        gbl_layerPanelContent.columnWidths = new int[]{0, 0, 0, 0};
        gbl_layerPanelContent.rowHeights = new int[]{0, 0, 0, 0};
        gbl_layerPanelContent.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_layerPanelContent.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        container.setLayout(gbl_layerPanelContent);

        this.selectAllCheckBox = new JCheckBox();
        this.selectAllCheckBox.setFocusable(false);
        GridBagConstraints gbc_selectAllCheckBox = new GridBagConstraints();
        gbc_selectAllCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_selectAllCheckBox.gridx = 0;
        gbc_selectAllCheckBox.gridy = 0;
        container.add(this.selectAllCheckBox, gbc_selectAllCheckBox);
        this.selectAllCheckBox.addActionListener((e) -> {
            if (this.selectAllCheckBox.isSelected()) {
                this.hasSelected = true;
                for (var jCheckBox : this.checkBoxList) {
                    jCheckBox.setSelected(true);
                }
            } else {
                this.hasSelected = false;
                for (var jCheckBox : this.checkBoxList) {
                    jCheckBox.setSelected(false);
                }
            }
            this.innerMqService.pub(Topic.DOWNLOAD_CONFIG_FRAME_ZOOM_SELECTED, this.hasSelected);
        });

        var zoomTitleLabel = new JLabel("全选");
        zoomTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        zoomTitleLabel.setFocusable(false);
        GridBagConstraints gbc_layerTitleLabel = new GridBagConstraints();
        gbc_layerTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_layerTitleLabel.gridx = 1;
        gbc_layerTitleLabel.gridy = 0;
        container.add(zoomTitleLabel, gbc_layerTitleLabel);

        for (var i = 0; i <= 21; i++) {
            var zoomEachCheckBox = new JCheckBox("");
            zoomEachCheckBox.setFocusable(false);
            GridBagConstraints gbc_layerEachCheckBox = new GridBagConstraints();
            gbc_layerEachCheckBox.insets = new Insets(0, 0, 5, 5);
            gbc_layerEachCheckBox.gridx = 0;
            gbc_layerEachCheckBox.gridy = i + 1;
            container.add(zoomEachCheckBox, gbc_layerEachCheckBox);
            this.checkBoxList.add(zoomEachCheckBox);
            zoomEachCheckBox.addActionListener((e) -> {
                if (zoomEachCheckBox.isSelected()) {
                    this.hasSelected = true;
                    if (isCheckBoxAllSelected()) {
                        this.selectAllCheckBox.setSelected(true);
                    }
                } else {
                    this.selectAllCheckBox.setSelected(false);
                }
                if (isCheckBoxNoneSelected()) {
                    this.hasSelected = false;
                }
                this.innerMqService.pub(Topic.DOWNLOAD_CONFIG_FRAME_ZOOM_SELECTED, this.hasSelected);
            });
            JLabel layerEachLabel = new JLabel(String.valueOf(i));
            layerEachLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
            GridBagConstraints gbc_layerEachLabel = new GridBagConstraints();
            gbc_layerEachLabel.insets = new Insets(0, 0, 5, 5);
            gbc_layerEachLabel.gridx = 1;
            gbc_layerEachLabel.gridy = i + 1;
            container.add(layerEachLabel, gbc_layerEachLabel);
        }

    }

    public ArrayList<Integer> getSelectedZooms() {
        var layers = new ArrayList<Integer>();
        for (var i = 0; i < this.checkBoxList.size(); i++) {
            if (this.checkBoxList.get(i).isSelected()) {
                layers.add(i);
            }
        }
        return layers;
    }

    public void removeAllSelectedZooms() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            this.selectAllCheckBox.setSelected(false);
            for (var checkBox : this.checkBoxList) {
                checkBox.setSelected(false);
            }
        });
    }

    private boolean isCheckBoxAllSelected() {
        var flag = true;
        for (var checkBox : checkBoxList) {
            if (!checkBox.isSelected()) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private boolean isCheckBoxNoneSelected() {
        var flag = true;
        for (var checkBox : checkBoxList) {
            if (checkBox.isSelected()) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private boolean isCheckBoxHasSelected() {
        var flag = false;
        for (var checkBox : checkBoxList) {
            if (checkBox.isSelected()) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}

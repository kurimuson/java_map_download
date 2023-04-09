package com.jmd.ui.common;

import com.jmd.common.StaticVar;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.Serial;

public class CommonContainerPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 6628330531744102560L;

    public CommonContainerPanel(String title) {
        if (title != null) {
            this.setBorder(new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP,
                    StaticVar.FONT_SourceHanSansCNNormal_12, null));
        }
        this.setLayout(new BorderLayout());
    }

    // 预留此方法的目的为适配 eclipse 的 window build 正确识别
    public void addContent(Component comp) {
        super.add(comp, BorderLayout.CENTER);
    }

}

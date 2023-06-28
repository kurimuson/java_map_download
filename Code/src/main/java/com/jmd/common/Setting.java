package com.jmd.common;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import com.jmd.model.setting.AddedLayerSetting;
import lombok.Data;

@Data
public class Setting implements Serializable {

    @Serial
    private static final long serialVersionUID = 2828730448277028688L;

    private Integer themeType;
    private String themeName;
    private String themeClazz;
    private Boolean floatingWindowShow;
    private String lastDirPath;
    private ArrayList<AddedLayerSetting> addedLayers;

    public Setting() {

    }

    public Setting(String type) {
        switch (type) {
            case "default":
                this.themeType = 3;
                this.themeName = "Flatlaf IntelliJ";
                this.themeClazz = "com.formdev.flatlaf.FlatIntelliJLaf";
                this.floatingWindowShow = true;
                this.addedLayers = new ArrayList<>();
                break;
            default:
                break;
        }
    }

}

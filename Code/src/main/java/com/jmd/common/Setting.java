package com.jmd.common;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import com.jmd.entity.setting.AddedLayerSetting;
import lombok.Data;

@Data
public class Setting implements Serializable {

	@Serial
	private static final long serialVersionUID = 2828730448277028688L;

	private Integer themeType;
	private String themeName;
	private String themeClazz;
	private String lastDirPath;

	private ArrayList<AddedLayerSetting> addedLayers;

}

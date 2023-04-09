package com.jmd.entity.task;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.jmd.entity.config.HttpClientConfigEntity;

import lombok.Data;

@Data
public class TaskAllInfoEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 6301744429585359593L;

	private HttpClientConfigEntity httpConfig;
	private String tileUrl;
	private String tileName;
	private String mapType;
	private Integer imgType;
	private String savePath;
	private String pathStyle;
	private Boolean isCoverExists;
	private Boolean isMergeTile;
	private Integer mergeType;
	private Long allRealCount;
	private Long allRunCount;
	private ConcurrentHashMap<Integer, TaskInstEntity> eachLayerTask;
	private ConcurrentHashMap<String, ErrorTileEntity> errorTiles;

}

package com.jmd.model.task;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

@Data
public class TaskAllInfoEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 6301744429585359593L;

	private String tileUrl;
	private String tileName;
	private String mapType;
	private Integer imgType;
	private String savePath;
	private String pathStyle;
	private Boolean isCoverExists;
	private Boolean isMergeTile;
	private Integer mergeType;
	private Integer errorHandlerType;
	private Long allRealCount;
	private Long allRunCount;
	private ConcurrentHashMap<Integer, TaskInstEntity> eachLayerTask;
	private ConcurrentHashMap<String, ErrorTileEntity> errorTiles;

}

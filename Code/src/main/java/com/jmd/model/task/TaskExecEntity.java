package com.jmd.model.task;

import java.util.List;

import com.jmd.callback.TileDownloadedCallback;
import com.jmd.model.geo.Polygon;

import lombok.Data;

@Data
public class TaskExecEntity {

	private String tileName;
	private Integer z;
	private Long xStart;
	private Long xEnd;
	private Long yStart;
	private Long yEnd;
	private Long xRun;
	private Long yRun;
	private Long startCount;
	private List<Polygon> polygons;
	private String downloadUrl;
	private Integer imgType;
	private String pathStyle;
	private String savePath;
	private Boolean isCoverExists;
	private TileDownloadedCallback tileCB;

}

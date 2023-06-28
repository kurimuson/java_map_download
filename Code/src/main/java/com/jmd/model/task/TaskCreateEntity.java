package com.jmd.model.task;

import java.util.List;

import com.jmd.model.geo.Polygon;

import lombok.Data;

@Data
public class TaskCreateEntity {

	private List<Integer> zoomList;
	private List<Polygon> polygons;
	private String tileUrl;
	private String savePath;
	private String tileName;
	private String mapType;
	private Integer imgType;
	private String pathStyle;
	private Boolean isCoverExists;
	private Boolean isMergeTile;
	private Integer mergeType;
	private Integer errorHandlerType;

}

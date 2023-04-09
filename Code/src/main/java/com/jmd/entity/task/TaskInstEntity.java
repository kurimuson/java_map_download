package com.jmd.entity.task;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.jmd.entity.geo.Polygon;

import lombok.Data;

@Data
public class TaskInstEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = -7964576148008799649L;

	private Integer z;
	private List<Polygon> polygons;
	private ArrayList<Long[]> divideX;
	private ArrayList<Long[]> divideY;
	private ConcurrentHashMap<String, TaskBlockEntity> blocks;
	private Long allCount;
	private Long realCount;

	private Long xStart;
	private Long xEnd;
	private Long yStart;
	private Long yEnd;

	private Boolean needMerge;
	private Boolean isMerged;

}

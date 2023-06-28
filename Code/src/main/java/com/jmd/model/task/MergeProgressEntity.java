package com.jmd.model.task;

import lombok.Data;

@Data
public class MergeProgressEntity {

	private Long allPixel;
	private Long runPixel;
	private Double perc;
	private Integer restSec;

}

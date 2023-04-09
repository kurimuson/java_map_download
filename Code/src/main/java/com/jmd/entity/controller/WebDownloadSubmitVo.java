package com.jmd.entity.controller;

import java.util.List;

import lombok.Data;

@Data
public class WebDownloadSubmitVo {

	private String tileName;
	private String mapType;
	private List<String> tileUrl;
	private List<List<double[]>> points;

}

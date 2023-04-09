package com.jmd.entity.district;

import java.util.List;

import lombok.Data;

@Data
public class WebAPIResult {

	private String name;
	private String citycode;
	private String adcode;
	private String polyline;
	private String center;
	private List<WebAPIResult> districts;

}

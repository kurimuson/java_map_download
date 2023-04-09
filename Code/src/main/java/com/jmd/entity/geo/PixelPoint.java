package com.jmd.entity.geo;

import lombok.Data;

@Data
public class PixelPoint {

	private Double x;
	private Double y;

	public PixelPoint() {

	}

	public PixelPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

}

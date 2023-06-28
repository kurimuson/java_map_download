package com.jmd.model.geo;

import java.io.Serializable;

import com.jmd.util.GeoUtils;

import lombok.Data;

@Data
public class MercatorPoint implements Serializable {

	private static final long serialVersionUID = 4066123199303085477L;

	private Double lng;
	private Double lat;

	public MercatorPoint() {

	}

	public MercatorPoint(double lng, double lat) {
		this.lng = lng;
		this.lat = lat;
	}

	public LngLatPoint convert2LngLat() {
		return GeoUtils.Mercator2LngLat(this);
	}

}

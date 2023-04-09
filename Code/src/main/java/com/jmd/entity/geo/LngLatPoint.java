package com.jmd.entity.geo;

import java.io.Serializable;

import com.jmd.util.GeoUtils;

import lombok.Data;

@Data
public class LngLatPoint implements Serializable {

	private static final long serialVersionUID = -3504030834818220866L;

	private Double lng;
	private Double lat;

	public LngLatPoint() {

	}

	public LngLatPoint(double lng, double lat) {
		this.lng = lng;
		this.lat = lat;
	}

	public MercatorPoint convert2Mercator() {
		return GeoUtils.LngLat2Mercator(this);
	}

}

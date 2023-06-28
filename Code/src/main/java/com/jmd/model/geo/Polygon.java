package com.jmd.model.geo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Polygon implements Serializable {

	private static final long serialVersionUID = 8429062630741585169L;

	ArrayList<MercatorPoint> path;

	public Polygon() {

	}

	public Polygon(List<MercatorPoint> path) {
		this.path = (ArrayList<MercatorPoint>) path;
	}

}

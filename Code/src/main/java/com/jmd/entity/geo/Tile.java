package com.jmd.entity.geo;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

@Data
public class Tile implements Serializable {

	@Serial
	private static final long serialVersionUID = -3598715656639420131L;

	private Integer z;
	private Long x;
	private Long y;

	private MercatorPoint topLeft;
	private MercatorPoint topRight;
	private MercatorPoint bottomLeft;
	private MercatorPoint bottomRight;

	public Tile() {

	}

	public Tile(int z, long x, long y) {
		this.z = z;
		this.x = x;
		this.y = y;
	}

}

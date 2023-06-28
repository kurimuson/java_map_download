package com.jmd.model.task;

import java.io.Serial;
import java.io.Serializable;

import com.jmd.model.geo.Tile;

import lombok.Data;

@Data
public class ErrorTileEntity implements Serializable{

	@Serial
	private static final long serialVersionUID = 3473523097592663626L;
	
	private String keyName;
	private String blockName;
	private Tile tile;

}

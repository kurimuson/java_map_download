package com.jmd.entity.result;

import lombok.Data;

@Data
public class DownloadResult {

	private byte[] imgData;
	private boolean success;

}

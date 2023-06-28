package com.jmd.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ImageMergeAsyncTaskResult extends BlockAsyncTaskResult {

	private Integer divideXIndex;
	private Integer divideYIndex;

}

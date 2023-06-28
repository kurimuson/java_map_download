package com.jmd.model.task;

import java.util.ArrayList;

import lombok.Data;

@Data
public class TaskBlockDivide {

	private Long countX;
	private Long countY;
	private ArrayList<Long[]> divideX;
	private ArrayList<Long[]> divideY;

}

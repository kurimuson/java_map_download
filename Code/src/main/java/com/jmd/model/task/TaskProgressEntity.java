package com.jmd.model.task;

import lombok.Data;

@Data
public class TaskProgressEntity {

	private Long lastCount;
	private Long currentCount;
	private Double perc;

	public TaskProgressEntity() {

	}

	public TaskProgressEntity(long lastCount, long currentCount, double perc) {
		this.lastCount = lastCount;
		this.currentCount = currentCount;
		this.perc = perc;
	}

}

package com.jmd.os;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

@Component
public class RAMMonitor {

	private DecimalFormat df1 = new DecimalFormat("#.#");

	public String getUsedRam() {
		double total = Double.valueOf(Runtime.getRuntime().totalMemory());
		return df1.format(total / 1024.0 / 1024.0) + "MB";
	}

}

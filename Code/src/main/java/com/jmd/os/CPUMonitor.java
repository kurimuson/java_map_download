package com.jmd.os;

import java.lang.management.ManagementFactory;

import org.springframework.stereotype.Component;

import com.sun.management.OperatingSystemMXBean;

@Component
public class CPUMonitor {

	private java.lang.management.OperatingSystemMXBean java_osMxBean = ManagementFactory.getOperatingSystemMXBean();
	private java.lang.management.ThreadMXBean java_threadBean = ManagementFactory.getThreadMXBean();
	private com.sun.management.OperatingSystemMXBean sun_osMxBean = (OperatingSystemMXBean) ManagementFactory
			.getOperatingSystemMXBean();
	private long preTime = System.nanoTime();
	private long preUsedTime = 0;

	public double getSystemCpuLoad() {
		return sun_osMxBean.getCpuLoad();
	}

	public double getProcessCpuLoad() {
		return sun_osMxBean.getProcessCpuLoad();
	}

	public double getProcessCpuUsage_Old() {
		long totalTime = 0;
		for (long id : java_threadBean.getAllThreadIds()) {
			totalTime += java_threadBean.getThreadCpuTime(id);
		}
		long curtime = System.nanoTime();
		long usedTime = totalTime - preUsedTime;
		long totalPassedTime = curtime - preTime;
		preTime = curtime;
		preUsedTime = totalTime;
		return (((double) usedTime) / totalPassedTime / java_osMxBean.getAvailableProcessors()) * 100;
	}

}

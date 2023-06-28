package com.jmd.async.task.scheduler;

import com.jmd.callback.DownloadMonitoringCallback;
import com.jmd.model.task.TaskAllInfoEntity;
import com.jmd.model.task.TaskBlockEntity;
import com.jmd.model.task.TaskInstEntity;
import com.jmd.model.task.TaskProgressEntity;

public class DownloadMonitoringInterval implements Runnable {

	private TaskAllInfoEntity taskAllInfo;
	private DownloadMonitoringCallback callBack;
	private long lastCount = 0;

	public DownloadMonitoringInterval(TaskAllInfoEntity taskAllInfo, DownloadMonitoringCallback callBack) {
		this.taskAllInfo = taskAllInfo;
		this.callBack = callBack;
	}

	@Override
	public void run() {
		var runCount = getRunCount();
		double allCount = taskAllInfo.getAllRealCount();
		double perc = (double) runCount / allCount;
		this.callBack.execute(new TaskProgressEntity(lastCount, runCount, perc));
		this.lastCount = runCount;
	}

	private long getRunCount() {
		var count = 0L;
		for (TaskInstEntity inst : taskAllInfo.getEachLayerTask().values()) {
			for (TaskBlockEntity block : inst.getBlocks().values()) {
				count = count + block.getRunCount();
			}
		}
		return count;
	}

}

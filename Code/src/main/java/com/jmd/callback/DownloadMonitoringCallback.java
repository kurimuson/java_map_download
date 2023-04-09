package com.jmd.callback;

import com.jmd.entity.task.TaskProgressEntity;

public interface DownloadMonitoringCallback {

	void execute(TaskProgressEntity progress);

}

package com.jmd.callback;

import com.jmd.model.task.TaskProgressEntity;

public interface DownloadMonitoringCallback {

	void execute(TaskProgressEntity progress);

}

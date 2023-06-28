package com.jmd.callback;

import com.jmd.model.task.MergeProgressEntity;

public interface TileMergeMonitoringCallback {

	void execute(MergeProgressEntity progress);

}

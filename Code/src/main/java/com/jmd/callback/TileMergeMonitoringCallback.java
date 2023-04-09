package com.jmd.callback;

import com.jmd.entity.task.MergeProgressEntity;

public interface TileMergeMonitoringCallback {

	void execute(MergeProgressEntity progress);

}

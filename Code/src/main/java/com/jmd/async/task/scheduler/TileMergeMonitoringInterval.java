package com.jmd.async.task.scheduler;

import com.jmd.callback.TileMergeMonitoringCallback;
import com.jmd.entity.task.MergeProgressEntity;
import com.jmd.taskfunc.TileMergeMatWrap;

public class TileMergeMonitoringInterval implements Runnable {

	private TileMergeMatWrap mat;

	private TileMergeMonitoringCallback tileMergeMonitoringCallback;

	public TileMergeMonitoringInterval(TileMergeMatWrap mat, TileMergeMonitoringCallback tileMergeMonitoringCallback) {
		this.mat = mat;
		this.tileMergeMonitoringCallback = tileMergeMonitoringCallback;
	}

	@Override
	public void run() {
		MergeProgressEntity progress = new MergeProgressEntity();
		progress.setAllPixel(mat.getAllPixel());
		progress.setRunPixel(mat.getRunPixel());
		if (mat.getAllPixel() == 0L) {
			progress.setPerc(0.0);
		} else {
			progress.setPerc((double) mat.getRunPixel() / (double) mat.getAllPixel());
		}
		this.tileMergeMonitoringCallback.execute(progress);
	}

}

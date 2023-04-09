package com.jmd.callback;

public interface TileDownloadedCallback {

	void execute(int z, String name, long count, long xRun, long yRun, boolean success);

}

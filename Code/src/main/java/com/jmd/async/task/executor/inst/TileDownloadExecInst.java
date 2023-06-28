package com.jmd.async.task.executor.inst;

import com.jmd.callback.TileDownloadedCallback;
import com.jmd.model.geo.Polygon;
import com.jmd.model.result.BlockAsyncTaskResult;
import com.jmd.model.result.DownloadResult;
import com.jmd.model.task.TaskExecEntity;
import com.jmd.http.HttpDownload;
import com.jmd.task.TaskState;
import com.jmd.util.CommonUtils;
import com.jmd.util.GeoUtils;
import com.jmd.util.TaskUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TileDownloadExecInst {

    private final int retry;
    private final HttpDownload download;
    private final TaskExecEntity execParam;

    private final List<Polygon> polygons;
    private final String tileName;
    private final String pathStyle;
    private final String savePath;
    private final Boolean isCoverExists;
    private final Integer imgType;
    private final TileDownloadedCallback callback;
    private final ArrayList<String> urls;

    public TileDownloadExecInst(int retry, HttpDownload download, TaskExecEntity execParam) {
        // 主体数据
        this.retry = retry;
        this.download = download;
        this.execParam = execParam;
        // 子选项数据
        this.polygons = this.execParam.getPolygons();
        this.tileName = this.execParam.getTileName();
        this.pathStyle = this.execParam.getPathStyle();
        this.savePath = this.execParam.getSavePath();
        this.isCoverExists = this.execParam.getIsCoverExists();
        this.imgType = this.execParam.getImgType();
        this.callback = this.execParam.getTileCB();
        this.urls = CommonUtils.expandUrl(this.execParam.getDownloadUrl());
    }

    public BlockAsyncTaskResult start() {
        var result = new BlockAsyncTaskResult();
        // 声明变量
        int z = this.execParam.getZ();
        long xStart = this.execParam.getXStart();
        long xEnd = this.execParam.getXEnd();
        long yStart = this.execParam.getYStart();
        long yEnd = this.execParam.getYEnd();
        long xRun = this.execParam.getXRun();
        long yRun = this.execParam.getYRun();
        // 存储返回值信息
        result.setXStart(xStart);
        result.setXEnd(xEnd);
        result.setYStart(yStart);
        result.setYEnd(yEnd);
        // 执行方法
        var urlIndex = 0;
        var name = z + "-" + xStart + "-" + xEnd + "-" + yStart + "-" + yEnd;
        var count = this.execParam.getStartCount();
        // 让每个线程不会在同一时间访问服务器
        try {
            var randomSleep = (int) (Math.random() * 500) + 10; // 10 - 500
            Thread.sleep(randomSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 循环开始
        for (var x = xStart; x <= xEnd; x++) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            if (x < xRun) {
                continue;
            }
            for (var y = yStart; y <= yEnd; y++) {
                // 任务取消
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                // 任务暂停
                if (TaskState.IS_PAUSING) {
                    while (TaskState.IS_PAUSING) {
                        this.taskWait();
                    }
                }
                if (xRun != xStart || yRun != yStart) {
                    if (x == xRun && y <= yRun) {
                        continue;
                    }
                }
                // 执行下载方法
                var res = this.checkAndDownload(urlIndex, z, x, y, count);
                count = res.count;
                // 使用下一个地址
                urlIndex = urlIndex + 1;
                if (urlIndex == urls.size()) {
                    urlIndex = 0;
                }
                // 回调
                this.callback.execute(z, name, count, x, y, res.success);
            }
        }
        result.setFlag(1);
        return result;
    }

    private void taskWait() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("暂停中取消任务");
        }
    }

    // 下载方法
    private Result checkAndDownload(int urlIndex, int z, long x, long y, long count) {
        var success = true;
        var isInFlag = this.isInCheck(z, x, y);
        var url = this.urls.get(urlIndex);
        if (isInFlag) {
            var pathAndName = this.savePath + TaskUtils.getFilePathName(this.pathStyle, this.imgType, z, x, y);
            if (this.isCoverExists) {
                // 覆盖：是
                var downloadResult = downloadTile(url, pathAndName, z, x, y);
                success = downloadResult.isSuccess();
            } else {
                // 覆盖：否
                var file = new File(pathAndName);
                if (!file.exists() || !file.isFile()) {
                    var downloadResult = downloadTile(url, pathAndName, z, x, y);
                    success = downloadResult.isSuccess();
                }
            }
            if (success) {
                count = count + 1;
            }
        }
        return new Result(success, count);
    }

    // 多边形检测
    private boolean isInCheck(int z, long x, long y) {
        var tile = GeoUtils.getTile(z, x, y);
        var f = false;
        for (var polygon : this.polygons) {
            f = GeoUtils.isTileInPolygon(tile, polygon) || GeoUtils.isPolygonInTile(tile, polygon);
            if (f) {
                break;
            }
        }
        return f;
    }

    // 下载
    private DownloadResult downloadTile(
            String downloadUrl,
            String pathAndName,
            int z, long x, long y
    ) {
        var url = CommonUtils.getDialectUrl(this.tileName, downloadUrl, z, x, y);
        return this.download.downloadTile(url, this.imgType, pathAndName, this.retry);
    }

    private static class Result {

        boolean success;
        long count;

        Result(boolean success, long count) {
            this.success = success;
            this.count = count;
        }

    }

}

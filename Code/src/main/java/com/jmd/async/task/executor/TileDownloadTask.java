package com.jmd.async.task.executor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import com.jmd.taskfunc.TaskState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.jmd.callback.TileDownloadedCallback;
import com.jmd.entity.geo.Polygon;
import com.jmd.entity.geo.Tile;
import com.jmd.entity.result.BlockAsyncTaskResult;
import com.jmd.entity.result.DownloadResult;
import com.jmd.entity.task.TaskExecEntity;
import com.jmd.http.HttpDownload;
import com.jmd.util.CommonUtils;
import com.jmd.util.GeoUtils;
import com.jmd.util.TaskUtils;

@Component
public class TileDownloadTask {

    @Value("${download.retry}")
    private int retry;

    @Autowired
    private HttpDownload download;

    @Async("TileDownloadExecutorPool")
    public Future<BlockAsyncTaskResult> exec(TaskExecEntity taskExec) {
        var result = new BlockAsyncTaskResult();
        // 声明变量
        int z = taskExec.getZ();
        long xStart = taskExec.getXStart();
        long xEnd = taskExec.getXEnd();
        long yStart = taskExec.getYStart();
        long yEnd = taskExec.getYEnd();
        long xRun = taskExec.getXRun();
        long yRun = taskExec.getYRun();
        var startCount = taskExec.getStartCount();
        var polygons = taskExec.getPolygons();
        var downloadUrl = taskExec.getDownloadUrl();
        var imgType = taskExec.getImgType();
        var tileName = taskExec.getTileName();
        var savePath = taskExec.getSavePath();
        var pathStyle = taskExec.getPathStyle();
        var isCoverExist = taskExec.getIsCoverExists();
        var callback = taskExec.getTileCB();
        var headers = taskExec.getHttpConfig().getHeaders();
        // 存储返回值信息
        result.setXStart(xStart);
        result.setXEnd(xEnd);
        result.setYStart(yStart);
        result.setYEnd(yEnd);
        // 执行方法
        var urls = CommonUtils.expandUrl(downloadUrl);
        var urlIndex = 0;
        var name = z + "-" + xStart + "-" + xEnd + "-" + yStart + "-" + yEnd;
        var count = startCount;
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
                if (TaskState.IS_TASK_PAUSING) {
                    do {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            System.out.println("暂停中取消任务");
                        }
                    } while (TaskState.IS_TASK_PAUSING);
                }
                if (xRun != xStart || yRun != yStart) {
                    if (x == xRun && y <= yRun) {
                        continue;
                    }
                }
                var tile = GeoUtils.getTile(z, x, y);
                var success = true;
                var isInFlag = false;
                for (var polygon : polygons) {
                    isInFlag = GeoUtils.isTileInPolygon(tile, polygon) || GeoUtils.isPolygonInTile(tile, polygon);
                    if (isInFlag) {
                        break;
                    }
                }
                var url = urls.get(urlIndex);
                if (isInFlag) {
                    var pathAndName = savePath + TaskUtils.getFilePathName(pathStyle, imgType, z, x, y);
                    if (isCoverExist) {
                        // 覆盖：是
                        var downloadResult = download(tileName, url, headers, pathStyle, imgType, pathAndName, z, x, y, retry);
                        success = downloadResult.isSuccess();
                    } else {
                        // 覆盖：否
                        var file = new File(pathAndName);
                        if (!file.exists() || !file.isFile()) {
                            var downloadResult = download(tileName, url, headers, pathStyle, imgType, pathAndName, z, x, y, retry);
                            success = downloadResult.isSuccess();
                        }
                    }
                    if (success) {
                        count = count + 1;
                    } else if (!Thread.currentThread().isInterrupted()) {
                        // System.out.println("Tile Download Error: " + "x-" + x + ",y-" + y + ",z-" + z);
                    }
                    urlIndex = urlIndex + 1;
                    if (urlIndex == urls.size()) {
                        urlIndex = 0;
                    }
                }
                callback.execute(z, name, count, x, y, success);
            }
        }
        result.setFlag(1);
        return new AsyncResult<>(result);
    }

    private DownloadResult download(
            String tileName,
            String downloadUrl,
            HashMap<String, String> headers,
            String pathStyle,
            int imgType, String pathAndName,
            int z, long x, long y,
            int retry
    ) {
        var url = CommonUtils.getDialectUrl(tileName, downloadUrl, z, x, y);
        return download.downloadTile(url, headers, imgType, pathAndName, retry);
    }

}

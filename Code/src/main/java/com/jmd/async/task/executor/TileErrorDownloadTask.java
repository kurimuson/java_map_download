package com.jmd.async.task.executor;

import java.util.List;
import java.util.concurrent.Future;

import com.jmd.task.TaskState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.jmd.model.result.BlockAsyncTaskResult;
import com.jmd.model.task.ErrorTileEntity;
import com.jmd.model.task.TaskAllInfoEntity;
import com.jmd.http.HttpDownload;
import com.jmd.util.CommonUtils;
import com.jmd.util.TaskUtils;

@Component
public class TileErrorDownloadTask {

    @Value("${download.retry}")
    private int retry;

    @Autowired
    private HttpDownload download;

    @Async("TileDownloadExecutorPool")
    public Future<BlockAsyncTaskResult> exec(TaskAllInfoEntity taskAllInfo, List<ErrorTileEntity> errorTileList) {
        var result = new BlockAsyncTaskResult();
        // 获取URL
        var urls = CommonUtils.expandUrl(taskAllInfo.getTileUrl());
        var urlIndex = 0;
        for (var errorTile : errorTileList) {
            // 任务取消
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            // 任务暂停
            if (TaskState.IS_PAUSING) {
                do {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("暂停中取消任务");
                    }
                } while (TaskState.IS_PAUSING);
            }
            // 声明变量
            var z = errorTile.getTile().getZ();
            var x = errorTile.getTile().getX();
            var y = errorTile.getTile().getY();
            var pathAndName = taskAllInfo.getSavePath() + TaskUtils.getFilePathName(taskAllInfo.getPathStyle(), taskAllInfo.getImgType(), z, x, y);
            var url = urls.get(urlIndex)
                    .replace("{z}", String.valueOf(z))
                    .replace("{x}", String.valueOf(x))
                    .replace("{y}", String.valueOf(y));
            // 下载瓦片
            var downloadResult = download.downloadTile(
                    url,
                    taskAllInfo.getImgType(),
                    pathAndName,
                    retry
            );
            boolean success = downloadResult.isSuccess();
            // 下载瓦片 - 结果
            if (success) {
                // 下载成功，删除ErrorTile
                taskAllInfo.getErrorTiles().remove(errorTile.getKeyName());
                // 更新进度
                var block = taskAllInfo.getEachLayerTask().get(z).getBlocks().get(errorTile.getBlockName());
                block.setRunCount(block.getRunCount() + 1);
                // 更新block信息
                taskAllInfo.getEachLayerTask().get(z).getBlocks().put(errorTile.getBlockName(), block);
            } else {
                // 下载失败，打印日志
                if (taskAllInfo.getErrorTiles().size() <= 50) {
                    System.out.println("Tile Download Error: " + url);
                }
            }
            // URL Index
            urlIndex = urlIndex + 1;
            if (urlIndex == urls.size()) {
                urlIndex = 0;
            }
            // 稳定下载，防止过快
            try {
                if (errorTileList.size() >= 50) {
                    Thread.sleep(100);
                } else if (errorTileList.size() >= 20) {
                    Thread.sleep(200);
                } else if (errorTileList.size() >= 10) {
                    Thread.sleep(300);
                } else {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new AsyncResult<>(result);
    }

}

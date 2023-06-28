package com.jmd.async.task.executor;

import java.util.concurrent.CompletableFuture;

import com.jmd.async.task.executor.inst.TileDownloadExecInst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.jmd.model.result.BlockAsyncTaskResult;
import com.jmd.model.task.TaskExecEntity;
import com.jmd.http.HttpDownload;

@Component
public class TileDownloadTask {

    @Value("${download.retry}")
    private int retry;

    @Autowired
    private HttpDownload download;

    @Async("TileDownloadExecutorPool")
    public CompletableFuture<BlockAsyncTaskResult> exec(TaskExecEntity execParam) {
        var inst = new TileDownloadExecInst(retry, download, execParam);
        var result = inst.start();
        return CompletableFuture.completedFuture(result);
    }

}

package com.jmd.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.jmd.callback.LogCallback;
import com.jmd.callback.TileMergeFirstFinishBack;
import com.jmd.rx.Topic;
import com.jmd.rx.service.InnerMqService;
import com.jmd.ui.common.CommonDialog;
import com.jmd.util.MyFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jmd.async.task.executor.TileCalculationTask;
import com.jmd.async.task.executor.TileDownloadTask;
import com.jmd.async.task.executor.TileErrorDownloadTask;
import com.jmd.async.task.executor.TileMergeTask;
import com.jmd.callback.LayerDownloadCallback;
import com.jmd.callback.TileDownloadedCallback;
import com.jmd.common.StaticVar;
import com.jmd.model.geo.Bound;
import com.jmd.model.geo.Polygon;
import com.jmd.model.geo.Tile;
import com.jmd.model.result.BlockAsyncTaskResult;
import com.jmd.model.result.ImageMergeAsyncTaskResult;
import com.jmd.model.task.ErrorTileEntity;
import com.jmd.model.task.TaskAllInfoEntity;
import com.jmd.model.task.TaskBlockDivide;
import com.jmd.model.task.TaskBlockEntity;
import com.jmd.model.task.TaskCreateEntity;
import com.jmd.model.task.TaskExecEntity;
import com.jmd.model.task.TaskInstEntity;
import com.jmd.http.HttpClient;
import com.jmd.util.GeoUtils;
import com.jmd.util.TaskUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskStepFunc {

    @Value("${tile.block-divide}")
    private int blockDivide;

    private final InnerMqService innerMqService = InnerMqService.getInstance();

    @Autowired
    private HttpClient httpClient;
    @Autowired
    private TileCalculationTask tileCalculationTask;
    @Autowired
    private TileDownloadTask tileDownloadTask;
    @Autowired
    private TileErrorDownloadTask tileErrorDownloadTask;
    @Autowired
    private TileMergeTask tileMergeTask;

    // 创建下载任务
    public TaskAllInfoEntity tileDownloadTaskCreate(TaskCreateEntity taskCreate) {
        TaskAllInfoEntity taskAllInfo = new TaskAllInfoEntity();
        ConcurrentHashMap<Integer, TaskInstEntity> eachLayerTask = new ConcurrentHashMap<>();
        this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "开始计算...");
        long count = 0L;
        long start = System.currentTimeMillis();
        for (int z : taskCreate.getZoomList()) {
            try {
                TaskInstEntity inst = tileTaskInstCalculation(z, taskCreate.getPolygons(), (e) -> {
                    this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, e);
                });
                inst.setNeedMerge(taskCreate.getIsMergeTile());
                inst.setIsMerged(false);
                count = count + inst.getRealCount();
                eachLayerTask.put(z, inst);
            } catch (InterruptedException | ExecutionException e) {
                log.error("Tile Calculation Error", e);
            }
        }
        long end = System.currentTimeMillis();
        taskAllInfo.setTileUrl(taskCreate.getTileUrl());
        taskAllInfo.setImgType(taskCreate.getImgType());
        taskAllInfo.setTileName(taskCreate.getTileName());
        taskAllInfo.setMapType(taskCreate.getMapType());
        taskAllInfo.setSavePath(taskCreate.getSavePath());
        taskAllInfo.setPathStyle(taskCreate.getPathStyle());
        taskAllInfo.setIsCoverExists(taskCreate.getIsCoverExists());
        taskAllInfo.setIsMergeTile(taskCreate.getIsMergeTile());
        taskAllInfo.setMergeType(taskCreate.getMergeType());
        taskAllInfo.setErrorHandlerType(taskCreate.getErrorHandlerType());
        taskAllInfo.setAllRealCount(count);
        taskAllInfo.setAllRunCount(0L);
        taskAllInfo.setEachLayerTask(eachLayerTask);
        taskAllInfo.setErrorTiles(new ConcurrentHashMap<>());
        this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "计算完成");
        this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "需要下载的总数：" + count + "，瓦片图计算所用时间：" + (end - start) / 1000 + "秒");
        this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "开始下载...");
        return taskAllInfo;
    }

    // 计算任务实例下载量
    public TaskInstEntity tileTaskInstCalculation(int zoom, List<Polygon> polygons, LogCallback logCB)
            throws InterruptedException, ExecutionException {
        Bound bound = GeoUtils.getPolygonsBound(polygons);
        Tile topLeftTile = GeoUtils.getTile(zoom, bound.getTopLeft());
        Tile bottomRightTile = GeoUtils.getTile(zoom, bound.getBottomRight());
        TaskBlockDivide divide = TaskUtils.blockDivide(topLeftTile.getX(), bottomRightTile.getX(), topLeftTile.getY(),
                bottomRightTile.getY(), Math.sqrt(blockDivide));
        ArrayList<Long[]> divideX = divide.getDivideX();
        ArrayList<Long[]> divideY = divide.getDivideY();
        long countX = divide.getCountX();
        long countY = divide.getCountY();
        List<Future<TaskBlockEntity>> futures = new ArrayList<>();
        for (Long[] x : divideX) {
            for (Long[] y : divideY) {
                Future<TaskBlockEntity> future = tileCalculationTask.exec(zoom, topLeftTile.getX() + x[0],
                        topLeftTile.getX() + x[1], topLeftTile.getY() + y[0],
                        topLeftTile.getY() + y[1], polygons);
                futures.add(future);
            }
        }
        TaskInstEntity inst = new TaskInstEntity();
        inst.setZ(zoom);
        inst.setPolygons(polygons);
        inst.setDivideX(divideX);
        inst.setDivideY(divideY);
        inst.setXStart(topLeftTile.getX());
        inst.setYStart(topLeftTile.getY());
        inst.setXEnd(bottomRightTile.getX());
        inst.setYEnd(bottomRightTile.getY());
        ConcurrentHashMap<String, TaskBlockEntity> blocks = new ConcurrentHashMap<>();
        long count = 0L;
        boolean isCanceled = false;
        for (Future<TaskBlockEntity> future : futures) {
            if (isCanceled) {
                break;
            }
            try {
                TaskBlockEntity block = future.get();
                if (block.getRealCount() > 0) {
                    blocks.put(block.getName(), block);
                    count = count + block.getRealCount();
                }
            } catch (InterruptedException e1) {
                isCanceled = true;
                for (Future<TaskBlockEntity> f : futures) {
                    f.cancel(true);
                }
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
        }
        inst.setRealCount(count);
        inst.setAllCount(countX * countY);
        inst.setBlocks(blocks);
        logCB.execute(zoom + "级该多边形bound内瓦片图总数" + inst.getAllCount() + "，需要下载的总数：" + count);
        return inst;
    }

    // 任务下载方法
    public void tileDownload(
            TaskAllInfoEntity taskAllInfo,
            LayerDownloadCallback layerStartBack,
            LayerDownloadCallback layerEndBack,
            TileDownloadedCallback tileCB
    ) {
        boolean isCanceled = false;
        for (var inst : taskAllInfo.getEachLayerTask().values()) {
            if (isCanceled || TaskState.IS_CANCEL) {
                break;
            }
            this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "正在下载第" + inst.getZ() + "级地图...");
            layerStartBack.execute(inst.getZ());
            // 分配多线程下载任务
            var futures = new ArrayList<Future<BlockAsyncTaskResult>>();
            for (TaskBlockEntity block : inst.getBlocks().values()) {
                long xStart = block.getXStart();
                long yStart = block.getYStart();
                long xRun = block.getXRun();
                long yRun = block.getYRun();
                long xEnd = block.getXEnd();
                long yEnd = block.getYEnd();
                long runCount = block.getRunCount();
                boolean addTaskFlag = false;
                if (xStart == xEnd || yStart == yEnd) {
                    if (xStart == xEnd && yStart == yEnd) {
                        addTaskFlag = true;
                    }
                    if (xStart == xEnd && yStart != yEnd) {
                        if (yRun != yEnd) {
                            addTaskFlag = true;
                        }
                    }
                    if (xStart != xEnd && yStart == yEnd) {
                        if (xRun != xEnd) {
                            addTaskFlag = true;
                        }
                    }
                } else {
                    if (xRun != xEnd || yRun != yEnd) {
                        addTaskFlag = true;
                    }
                }
                if (addTaskFlag) {
                    var execParam = new TaskExecEntity();
                    execParam.setZ(inst.getZ());
                    execParam.setXStart(xStart);
                    execParam.setXEnd(xEnd);
                    execParam.setYStart(yStart);
                    execParam.setYEnd(yEnd);
                    execParam.setXRun(xRun);
                    execParam.setYRun(yRun);
                    execParam.setStartCount(runCount);
                    execParam.setPolygons(inst.getPolygons());
                    execParam.setTileName(taskAllInfo.getTileName());
                    execParam.setDownloadUrl(taskAllInfo.getTileUrl());
                    execParam.setImgType(taskAllInfo.getImgType());
                    execParam.setSavePath(taskAllInfo.getSavePath());
                    execParam.setPathStyle(taskAllInfo.getPathStyle());
                    execParam.setIsCoverExists(taskAllInfo.getIsCoverExists());
                    execParam.setTileCB(tileCB);
                    var future = tileDownloadTask.exec(execParam);
                    futures.add(future);
                }
            }
            for (var future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e1) {
                    isCanceled = true;
                    for (var f : futures) {
                        f.cancel(true);
                    }
                    break;
                } catch (ExecutionException e2) {
                    e2.printStackTrace();
                }
            }
            if (!isCanceled) {
                layerEndBack.execute(inst.getZ());
                this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "第" + inst.getZ() + "级地图下载完成");
            }
        }
    }

    // 错误瓦片下载方法
    public void tileErrorDownload(TaskAllInfoEntity taskAllInfo, int count) {
        if (taskAllInfo.getErrorTiles().size() == 0) {
            return;
        }
        if (taskAllInfo.getErrorHandlerType() == 3) {
            // 不处理，直接跳过
            this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "该层级有" + taskAllInfo.getErrorTiles().size() + "张错误瓦片未下载，用户选择跳过");
            for (var entry : taskAllInfo.getErrorTiles().entrySet()) {
                // 删除ErrorTile
                taskAllInfo.getErrorTiles().remove(entry.getValue().getKeyName());
                // 更新进度
                var z = entry.getValue().getTile().getZ();
                var block = taskAllInfo.getEachLayerTask().get(z).getBlocks().get(entry.getValue().getBlockName());
                block.setRunCount(block.getRunCount() + 1);
                // 更新block信息
                taskAllInfo.getEachLayerTask().get(z).getBlocks().put(entry.getValue().getBlockName(), block);
            }
            return;
        }
        var isCanceled = false;
        this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "正在重新下载该层级错误瓦片，剩余数量：" + taskAllInfo.getErrorTiles().size());
        var eachDivide = 0;
        if ((double) taskAllInfo.getErrorTiles().size() / (double) blockDivide <= 10.0) {
            eachDivide = 10;
        } else if ((double) taskAllInfo.getErrorTiles().size() / (double) blockDivide <= 15.0) {
            eachDivide = 15;
        } else if ((double) taskAllInfo.getErrorTiles().size() / (double) blockDivide <= 20.0) {
            eachDivide = 20;
        } else {
            eachDivide = taskAllInfo.getErrorTiles().size() / blockDivide;
        }
        // 分配多线程下载任务
        var errorTileList = new ArrayList<ArrayList<ErrorTileEntity>>();
        var futures = new ArrayList<Future<BlockAsyncTaskResult>>();
        var mapIndex = 0;
        var listIndex = 0;
        for (var entry : taskAllInfo.getErrorTiles().entrySet()) {
            if (errorTileList.size() <= listIndex) {
                errorTileList.add(new ArrayList<>());
            }
            errorTileList.get(listIndex).add(entry.getValue());
            mapIndex = mapIndex + 1;
            if (mapIndex % eachDivide == 0) {
                listIndex = listIndex + 1;
            }
        }
        for (var list : errorTileList) {
            var future = tileErrorDownloadTask.exec(taskAllInfo, list);
            futures.add(future);
        }
        for (var future : futures) {
            try {
                future.get();
            } catch (InterruptedException e1) {
                isCanceled = true;
                for (var f : futures) {
                    f.cancel(true);
                }
                break;
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
        }
        // 若仍然有未下载的瓦片，递归重复执行此方法
        if (!isCanceled && taskAllInfo.getErrorTiles().size() > 0) {
            try {
                if (taskAllInfo.getErrorTiles().size() >= 6) {
                    Thread.sleep(1500);
                } else if (taskAllInfo.getErrorTiles().size() >= 3) {
                    Thread.sleep(3000);
                } else if (taskAllInfo.getErrorTiles().size() == 1) {
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count += 1;
            if (taskAllInfo.getErrorHandlerType() == 1) {
                // 循环重试下载（失败10次后等待10分钟，继续循环重试）
                if (count >= 10) {
                    this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "连续失败10次，等待10分钟后将继续下载");
                    count = 0;
                    try {
                        Thread.sleep(1000 * 60 * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tileErrorDownload(taskAllInfo, count);
            } else if (taskAllInfo.getErrorHandlerType() == 2) {
                // 失败5次后弹窗询问是否跳过
                if (count >= 5) {
                    var flag = CommonDialog.confirm("选择", "连续失败5次，是否跳过该层级？");
                    if (flag) {
                        return;
                    }
                    count = 0;
                    try {
                        Thread.sleep(1000 * 60 * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tileErrorDownload(taskAllInfo, count);
            }
        }
    }

    // 合并图片
    public void mergeTileImage(TileMergeMatWrap mat, TaskAllInfoEntity taskAllInfo, int zoom, TileMergeFirstFinishBack finishBack) {
        if (zoom == 0) {
            return;
        }
        try {
            this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "正在合并第" + zoom + "级地图，请勿关闭程序 implemented by OpenCV");
            // 声明变量
            var taskInst = taskAllInfo.getEachLayerTask().get(zoom);
            var z = taskInst.getZ();
            long xStart = taskInst.getXStart();
            long xEnd = taskInst.getXEnd();
            long yStart = taskInst.getYStart();
            long yEnd = taskInst.getYEnd();
            long mergeImageWidth = StaticVar.TILE_WIDTH * (xEnd - xStart + 1);
            long mergeImageHeight = StaticVar.TILE_HEIGHT * (yEnd - yStart + 1);
            if (mergeImageWidth >= Integer.MAX_VALUE || mergeImageHeight >= Integer.MAX_VALUE) {
                this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "合并后的图片width：" + mergeImageWidth + "，height：" + mergeImageHeight + "，宽度或高度大于int最大值" + Integer.MAX_VALUE + "，不予合并。");
                return;
            }
            var xiangsudaxiao = mergeImageWidth * mergeImageHeight;
            this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "合并后的图片width：" + mergeImageWidth + "，height：" + mergeImageHeight + "，像素大小："
                    + xiangsudaxiao);
            if (xiangsudaxiao > (long) Integer.MAX_VALUE) {
                this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "该" + zoom + "级地图合并后像素大小大于int最大值" + Integer.MAX_VALUE + "，合并时间可能会稍长，建议低配置电脑不要执行超大尺寸合并");
            }
            // 开启线程
            mat.init((int) mergeImageWidth, (int) mergeImageHeight);
            var cpuCoreCount = Runtime.getRuntime().availableProcessors();
            var d = Math.floor(Math.sqrt(cpuCoreCount));
            var divide = TaskUtils.blockDivide(xStart, xEnd, yStart, yEnd, d);
            var divideX = divide.getDivideX();
            var divideY = divide.getDivideY();
            var futures = new ArrayList<Future<ImageMergeAsyncTaskResult>>();
            for (var i = 0; i < divideX.size(); i++) {
                for (var j = 0; j < divideY.size(); j++) {
                    var future = tileMergeTask.exec(mat, z, xStart, yStart,
                            xStart + divideX.get(i)[0], xStart + divideX.get(i)[1], yStart + divideY.get(j)[0],
                            yStart + divideY.get(j)[1], taskInst.getPolygons(), taskAllInfo.getImgType(),
                            taskAllInfo.getSavePath(), taskAllInfo.getPathStyle(), i, j);
                    futures.add(future);
                }
            }
            for (var future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            finishBack.execute();
            this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "正在写入至硬盘...");
            // 文件类型
            var outPath = MyFileUtils.checkFilePath(taskAllInfo.getSavePath() + "/tile-merge" + "/");
            var outName = "z=" + z;
            // opencv导出
            mat.output(outPath, outName, taskAllInfo.getMergeType());
            mat.destroy();
            this.innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "第" + zoom + "级地图合并完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

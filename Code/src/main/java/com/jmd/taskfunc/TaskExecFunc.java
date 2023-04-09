package com.jmd.taskfunc;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;

import com.jmd.async.pool.scheduler.IntervalConfig;
import com.jmd.rx.Topic;
import com.jmd.rx.service.InnerMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.jmd.async.pool.executor.TileDownloadExecutorPool;
import com.jmd.async.pool.executor.TileMergeExecutorPool;
import com.jmd.async.task.scheduler.DownloadMonitoringInterval;
import com.jmd.async.task.scheduler.TileMergeMonitoringInterval;
import com.jmd.entity.geo.Tile;
import com.jmd.entity.task.ErrorTileEntity;
import com.jmd.entity.task.TaskAllInfoEntity;
import com.jmd.entity.task.TaskBlockEntity;
import com.jmd.entity.task.TaskCreateEntity;
import com.jmd.entity.task.TaskInstEntity;
import com.jmd.entity.task.TaskProgressEntity;
import com.jmd.inst.DownloadAmountInstance;
import com.jmd.os.CPUMonitor;
import com.jmd.os.RAMMonitor;
import com.jmd.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskExecFunc {

    @Value("${download.retry}")
    private int retry;

    private final InnerMqService innerMqService = InnerMqService.getInstance();

    @Lazy
    @Autowired
    private TaskStepFunc taskStep;
    @Autowired
    private TileDownloadExecutorPool tileDownloadExecutorPool;
    @Autowired
    private TileMergeExecutorPool tileMergeExecutorPool;
    @Autowired
    private DownloadAmountInstance downloadAmountInstance;
    @Autowired
    private CPUMonitor cpuMonitor;
    @Autowired
    private RAMMonitor ramMonitor;

    private final DecimalFormat df1 = new DecimalFormat("#.#");
    private final DecimalFormat df2 = new DecimalFormat("#.##");
    private boolean isHandlerCancel = false;
    private SwingWorker<Void, Void> downloadWorker;

    /**
     * 创建任务
     */
    public void createTask(TaskCreateEntity taskCreate) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                innerMqService.pub(Topic.DOWNLOAD_CONSOLE_CLEAR, true);
                // 显示任务信息
                innerMqService.pub(Topic.DOWNLOAD_CONSOLE_PROGRESS, 0);
                innerMqService.pub(Topic.TASK_STATUS_CURRENT, "正在计算下载量");
                innerMqService.pub(Topic.TASK_STATUS_MAP_TYPE, taskCreate.getMapType());
                innerMqService.pub(Topic.TASK_STATUS_LAYERS, taskCreate.getZoomList().toString());
                innerMqService.pub(Topic.TASK_STATUS_SAVE_PATH, taskCreate.getSavePath());
                innerMqService.pub(Topic.TASK_STATUS_PATH_STYLE, taskCreate.getPathStyle());
                if (taskCreate.getIsCoverExists()) {
                    innerMqService.pub(Topic.TASK_STATUS_IS_COVER_EXIST, "是");
                } else {
                    innerMqService.pub(Topic.TASK_STATUS_IS_COVER_EXIST, "否");
                }
                switch (taskCreate.getImgType()) {
                    case 0 -> innerMqService.pub(Topic.TASK_STATUS_IMG_TYPE, "PNG");
                    case 1 -> innerMqService.pub(Topic.TASK_STATUS_IMG_TYPE, "WEBP");
                    case 2 -> innerMqService.pub(Topic.TASK_STATUS_IMG_TYPE, "JPG - 低质量");
                    case 3 -> innerMqService.pub(Topic.TASK_STATUS_IMG_TYPE, "JPG - 中等质量");
                    case 4 -> innerMqService.pub(Topic.TASK_STATUS_IMG_TYPE, "JPG - 高质量");
                    default -> {
                    }
                }
                // 生成任务
                TaskAllInfoEntity taskAllInfo = taskStep.tileDownloadTaskCreate(taskCreate, (e) -> {
                    innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, e); // 日志回调
                });
                // 保存下载任务
                saveTaskFile(taskAllInfo);
                // 显示任务信息
                innerMqService.pub(Topic.TASK_STATUS_TILE_ALL_COUNT, String.valueOf(taskAllInfo.getAllRealCount()));
                innerMqService.pub(Topic.TASK_STATUS_TILE_DOWNLOADED_COUNT, "0");
                innerMqService.pub(Topic.TASK_STATUS_PROGRESS, "0%");
                // 开始下载
                downloadTask(taskAllInfo);
                return null;
            }

        };
        worker.execute();
    }

    /**
     * 加载任务
     */
    public void loadTask(TaskAllInfoEntity taskAllInfo) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                innerMqService.pub(Topic.DOWNLOAD_CONSOLE_CLEAR, true); // 日志回调
                innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "导入下载任务...");
                List<Integer> zoomList = new ArrayList<>(taskAllInfo.getEachLayerTask().keySet());
                // 显示任务信息
                innerMqService.pub(Topic.TASK_STATUS_CURRENT, "正在导入下载任务");
                innerMqService.pub(Topic.TASK_STATUS_MAP_TYPE, taskAllInfo.getMapType());
                innerMqService.pub(Topic.TASK_STATUS_LAYERS, zoomList.toString());
                innerMqService.pub(Topic.TASK_STATUS_SAVE_PATH, taskAllInfo.getSavePath());
                innerMqService.pub(Topic.TASK_STATUS_PATH_STYLE, taskAllInfo.getPathStyle());
                if (taskAllInfo.getIsCoverExists()) {
                    innerMqService.pub(Topic.TASK_STATUS_IS_COVER_EXIST, "是");
                } else {
                    innerMqService.pub(Topic.TASK_STATUS_IS_COVER_EXIST, "否");
                }
                switch (taskAllInfo.getImgType()) {
                    case 0 -> innerMqService.pub(Topic.TASK_STATUS_IMG_TYPE, "PNG");
                    case 1 -> innerMqService.pub(Topic.TASK_STATUS_IMG_TYPE, "WEBP");
                    case 2 -> innerMqService.pub(Topic.TASK_STATUS_IMG_TYPE, "JPG - 低质量");
                    case 3 -> innerMqService.pub(Topic.TASK_STATUS_IMG_TYPE, "JPG - 中等质量");
                    case 4 -> innerMqService.pub(Topic.TASK_STATUS_IMG_TYPE, "JPG - 高质量");
                    default -> {
                    }
                }
                innerMqService.pub(Topic.TASK_STATUS_TILE_ALL_COUNT, String.valueOf(taskAllInfo.getAllRealCount()));
                updateDownloadProcess(new TaskProgressEntity(0, taskAllInfo.getAllRunCount(), (double) taskAllInfo.getAllRunCount() / (double) taskAllInfo.getAllRealCount()));
                // 显示日志
                for (TaskInstEntity inst : taskAllInfo.getEachLayerTask().values()) {
                    String log = inst.getZ() + "级该多边形内总数" + inst.getAllCount() + "，需要下载的总数：" + inst.getRealCount();
                    innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, log);
                }
                // 开始下载
                downloadTask(taskAllInfo);
                return null;
            }

        };
        worker.execute();
    }

    /**
     * 开始下载
     */
    public void downloadTask(TaskAllInfoEntity _taskAllInfo) {
        int id = new Random().nextInt(1000000000);
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // 下载开始
                TaskAllInfoEntity[] taskAllInfo = new TaskAllInfoEntity[1];
                taskAllInfo[0] = _taskAllInfo;
                TaskState.IS_TASKING = true;
                downloadStart();
                // 配置HTTP
                taskAllInfo[0] = taskStep.setHttpConfig(taskAllInfo[0], (e) -> innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, e));
                // 开启定时任务
                innerMqService.pub(Topic.SET_INTERVAL, new IntervalConfig(id, new DownloadMonitoringInterval(taskAllInfo[0], (progress) -> taskSecInterval(taskAllInfo[0], progress)), 1000L));
                // 下载地图
                taskStep.tileDownload(taskAllInfo[0], (z) -> {
                    // 层级下载开始回调
                    innerMqService.pub(Topic.TASK_STATUS_CURRENT, "正在下载第" + z + "级地图");
                }, (z) -> {
                    // 层级下载结束回调 - 下载错误瓦片
                    eachLayerDownloadedTileErrorCallback(taskAllInfo[0]);
                    // 层级下载结束回调 - 合并图片
                    eachLayerDownloadedTileMergeCallback(taskAllInfo[0], z);
                }, (z, name, count, xRun, yRun, success) -> {
                    // 瓦片图下载回调
                    eachTileDownloadedCallback(taskAllInfo[0], z, name, count, xRun, yRun, success);
                }, (e) -> {
                    // 日志回调
                    innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, e);
                });
                // 下载结束
                TaskState.IS_TASKING = false;
                innerMqService.pub(Topic.CLEAR_INTERVAL, id);
                downloadFinish(taskAllInfo[0]);
                System.gc();
                return null;
            }

        };
        worker.execute();
        downloadWorker = worker;
    }

    /**
     * 任务每秒回调
     */
    private void taskSecInterval(TaskAllInfoEntity taskAllInfo, TaskProgressEntity progress) {
        // 显示资源使用率
        innerMqService.pub(Topic.RESOURCE_USAGE_THREAD_COUNT, String.valueOf(tileDownloadExecutorPool.getActiveCount()));
        innerMqService.pub(Topic.RESOURCE_USAGE_DOWNLOAD_SPEED, downloadAmountInstance.getDiffValue() + "/s");
        innerMqService.pub(Topic.RESOURCE_USAGE_DOWNLOAD_PER_SEC_COUNT, String.valueOf(progress.getCurrentCount() - progress.getLastCount()));
        downloadAmountInstance.saveLast();
        // 计算CPU使用率
        double sysLoad = cpuMonitor.getSystemCpuLoad();
        double proLoad = cpuMonitor.getProcessCpuLoad();
        innerMqService.pub(Topic.CPU_PERCENTAGE_DRAW_SYSTEM, sysLoad);
        innerMqService.pub(Topic.CPU_PERCENTAGE_DRAW_PROCESS, proLoad);
        innerMqService.pub(Topic.RESOURCE_USAGE_SYSTEM_CPU_USAGE, df1.format(sysLoad * 100) + "%");
        innerMqService.pub(Topic.RESOURCE_USAGE_PROCESS_CPU_USAGE, df1.format(proLoad * 100) + "%");
        if (TaskState.IS_TASKING) { // 防止任务完成后跳回上一秒的状态
            // 更新下载进度
            updateDownloadProcess(progress);
        }
        // 保存下载任务
        if (TaskState.IS_TASKING) {
            taskAllInfo.setAllRunCount(progress.getCurrentCount());
            saveTaskFile(taskAllInfo);
        } else {
            if (!this.isHandlerCancel) {
                deleteTaskFile(taskAllInfo);
            }
        }
    }

    /**
     * 瓦片图下载回调
     */
    private void eachTileDownloadedCallback(TaskAllInfoEntity taskAllInfo, int z, String name, long count, long xRun, long yRun, boolean success) {
        if (!success) {
            String key = z + "-" + xRun + "-" + yRun;
            ErrorTileEntity errorTile = new ErrorTileEntity();
            errorTile.setKeyName(key);
            errorTile.setBlockName(name);
            errorTile.setTile(new Tile(z, xRun, yRun));
            taskAllInfo.getErrorTiles().put(key, errorTile);
        }
        TaskBlockEntity block = taskAllInfo.getEachLayerTask().get(z).getBlocks().get(name);
        block.setRunCount(count);
        block.setXRun(xRun);
        block.setYRun(yRun);
        taskAllInfo.getEachLayerTask().get(z).getBlocks().put(name, block);
    }

    /**
     * 层级下载结束回调 - 下载错误瓦片
     */
    private void eachLayerDownloadedTileErrorCallback(TaskAllInfoEntity taskAllInfo) {
        if (isHandlerCancel) {
            return;
        }
        if (taskAllInfo.getErrorTiles().size() == 0) {
            return;
        }
        taskStep.tileErrorDownload(taskAllInfo, (e) -> innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, e));
    }

    /**
     * 层级下载结束回调 - 合并图片
     */
    private void eachLayerDownloadedTileMergeCallback(TaskAllInfoEntity taskAllInfo, int z) {
        if (isHandlerCancel) {
            return;
        }
        if (z == 0) {
            return;
        }
        int id = new Random().nextInt(1000000000);
        if (taskAllInfo.getIsMergeTile() && !taskAllInfo.getEachLayerTask().get(z).getIsMerged()) {
            innerMqService.pub(Topic.TASK_STATUS_CURRENT, "正在合成第" + z + "级地图");
            // 声明Mat
            TileMergeMatWrap mat = new TileMergeMatWrap();
            // 合并进度监视定时器
            innerMqService.pub(Topic.SET_INTERVAL, new IntervalConfig(id, new TileMergeMonitoringInterval(mat, (progress) -> {
                innerMqService.pub(Topic.TILE_MERGE_PROCESS_PIXEL_COUNT, progress.getRunPixel() + "/" + progress.getAllPixel());
                innerMqService.pub(Topic.TILE_MERGE_PROCESS_THREAD, String.valueOf(tileMergeExecutorPool.getActiveCount()));
                innerMqService.pub(Topic.TILE_MERGE_PROCESS_PROGRESS, df2.format(progress.getPerc() * 100) + "%");
            }), 100L));
            // 开始合并
            taskStep.mergeTileImage(mat, taskAllInfo, z, (e) -> innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, e), (e) -> {
                innerMqService.pub(Topic.TILE_MERGE_PROCESS_PIXEL_COUNT, mat.getAllPixel() + "/" + mat.getAllPixel());
                innerMqService.pub(Topic.TILE_MERGE_PROCESS_THREAD, "0");
                innerMqService.pub(Topic.TILE_MERGE_PROCESS_PROGRESS, "100.00%");
                // 结束合并进度监视定时器
                innerMqService.pub(Topic.CLEAR_INTERVAL, id);
            });
            // 完成设置true
            taskAllInfo.getEachLayerTask().get(z).setIsMerged(true);
        }
    }

    /**
     * 更新下载进度
     */
    private void updateDownloadProcess(TaskProgressEntity progress) {
        innerMqService.pub(Topic.TASK_STATUS_TILE_DOWNLOADED_COUNT, String.valueOf(progress.getCurrentCount()));
        innerMqService.pub(Topic.TASK_STATUS_PROGRESS, df2.format(100 * progress.getPerc()) + "%");
        innerMqService.pub(Topic.DOWNLOAD_CONSOLE_PROGRESS, (int) Math.round(100 * progress.getPerc()));
    }

    /**
     * 下载开始
     */
    private void downloadStart() {
        TaskState.IS_TASK_PAUSING = false;
        isHandlerCancel = false;
        innerMqService.pub(Topic.CPU_PERCENTAGE_CLEAR, true);
        innerMqService.pub(Topic.RESOURCE_USAGE_CLEAR, true);
        innerMqService.pub(Topic.TILE_MERGE_PROCESS_CLEAR, true);
        innerMqService.pub(Topic.DOWNLOAD_CONSOLE_PAUSE_BUTTON_STATE, true);
        innerMqService.pub(Topic.DOWNLOAD_CONSOLE_CANCEL_BUTTON_STATE, true);
        innerMqService.pub(Topic.TASK_STATUS_CURRENT, "正在下载");
        downloadAmountInstance.reset();
    }

    /**
     * 下载结束
     */
    private void downloadFinish(TaskAllInfoEntity taskAllInfo) {
        TaskState.IS_TASK_PAUSING = false;
        downloadWorker = null;
        innerMqService.pub(Topic.RESOURCE_USAGE_CLEAR, true);
        innerMqService.pub(Topic.DOWNLOAD_CONSOLE_PAUSE_BUTTON_TEXT, "暂停任务");
        innerMqService.pub(Topic.DOWNLOAD_CONSOLE_PAUSE_BUTTON_STATE, false);
        innerMqService.pub(Topic.DOWNLOAD_CONSOLE_CANCEL_BUTTON_STATE, false);
        if (!isHandlerCancel) {
            long allRunCount = 0L;
            for (TaskInstEntity inst : taskAllInfo.getEachLayerTask().values()) {
                for (TaskBlockEntity block : inst.getBlocks().values()) {
                    allRunCount = allRunCount + block.getRunCount();
                }
            }
            taskAllInfo.setAllRunCount(allRunCount);
            updateDownloadProcess(new TaskProgressEntity(0, taskAllInfo.getAllRunCount(), (double) taskAllInfo.getAllRunCount() / (double) taskAllInfo.getAllRealCount()));
            innerMqService.pub(Topic.TASK_STATUS_CURRENT, "下载完成");
            innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "下载完成");
            System.out.println("[下载完成]");
            deleteTaskFile(taskAllInfo);
        } else {
            innerMqService.pub(Topic.TASK_STATUS_CURRENT, "下载任务已取消");
            innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "下载任务已取消");
            System.out.println("[下载任务已取消]");
        }
    }

    /**
     * 是否取消
     */
    public boolean isCancel() {
        return this.isHandlerCancel;
    }

    /**
     * 暂停下载任务
     */
    public void pauseTask() {
        if (TaskState.IS_TASKING && downloadWorker != null) {
            if (TaskState.IS_TASK_PAUSING) { // 继续
                TaskState.IS_TASK_PAUSING = false;
                innerMqService.pub(Topic.DOWNLOAD_CONSOLE_PAUSE_BUTTON_TEXT, "暂停任务");
                innerMqService.pub(Topic.TASK_STATUS_CURRENT, "继续下载");
                innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "继续下载任务");
            } else { // 暂停
                TaskState.IS_TASK_PAUSING = true;
                innerMqService.pub(Topic.DOWNLOAD_CONSOLE_PAUSE_BUTTON_TEXT, "继续任务");
                innerMqService.pub(Topic.TASK_STATUS_CURRENT, "暂停下载");
                innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "已暂停下载任务");
            }
        }
    }

    /**
     * 取消下载任务
     */
    public void cancelTaks() {
        if (TaskState.IS_TASKING && downloadWorker != null) {
            innerMqService.pub(Topic.DOWNLOAD_CONSOLE_CANCEL_BUTTON_STATE, true);
            innerMqService.pub(Topic.TASK_STATUS_CURRENT, "正在取消下载任务...");
            innerMqService.pub(Topic.DOWNLOAD_CONSOLE_LOG, "正在取消下载任务...");
            downloadWorker.cancel(true);
            this.isHandlerCancel = true;
            TaskState.IS_TASKING = false;
        }
    }

    /**
     * 保存下载任务
     */
    private void saveTaskFile(TaskAllInfoEntity taskAllInfo) {
        try {
            CommonUtils.saveObj2File(taskAllInfo, taskAllInfo.getSavePath() + "/task_info.jmd");
        } catch (IOException e) {
            log.error("Task File Save Error", e);
        }
    }

    /**
     * 删除下载任务
     */
    private void deleteTaskFile(TaskAllInfoEntity taskAllInfo) {
        File file = new File(taskAllInfo.getSavePath() + "/task_info.jmd");
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

}

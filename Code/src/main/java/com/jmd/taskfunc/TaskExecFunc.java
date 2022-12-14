package com.jmd.taskfunc;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;

import com.jmd.async.pool.scheduler.IntervalConfig;
import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
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

    @Lazy
    @Autowired
    private TaskStepFunc taskStep;

    @Autowired
    private SharedService sharedService;
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
     * ????????????
     */
    public void createTask(TaskCreateEntity taskCreate) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                sharedService.pub(SharedType.DOWNLOAD_CONSOLE_CLEAR, true);
                // ??????????????????
                sharedService.pub(SharedType.DOWNLOAD_CONSOLE_PROGRESS, 0);
                sharedService.pub(SharedType.TASK_STATUS_CURRENT, "?????????????????????");
                sharedService.pub(SharedType.TASK_STATUS_MAP_TYPE, taskCreate.getMapType());
                sharedService.pub(SharedType.TASK_STATUS_LAYERS, taskCreate.getZoomList().toString());
                sharedService.pub(SharedType.TASK_STATUS_SAVE_PATH, taskCreate.getSavePath());
                sharedService.pub(SharedType.TASK_STATUS_PATH_STYLE, taskCreate.getPathStyle());
                if (taskCreate.getIsCoverExists()) {
                    sharedService.pub(SharedType.TASK_STATUS_IS_COVER_EXIST, "???");
                } else {
                    sharedService.pub(SharedType.TASK_STATUS_IS_COVER_EXIST, "???");
                }
                switch (taskCreate.getImgType()) {
                    case 0 -> sharedService.pub(SharedType.TASK_STATUS_IMG_TYPE, "PNG");
                    case 1 -> sharedService.pub(SharedType.TASK_STATUS_IMG_TYPE, "JPG - ?????????");
                    case 2 -> sharedService.pub(SharedType.TASK_STATUS_IMG_TYPE, "JPG - ????????????");
                    case 3 -> sharedService.pub(SharedType.TASK_STATUS_IMG_TYPE, "JPG - ?????????");
                    default -> {
                    }
                }
                // ????????????
                TaskAllInfoEntity taskAllInfo = taskStep.tileDownloadTaskCreate(taskCreate, (e) -> {
                    sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, e); // ????????????
                });
                // ??????????????????
                saveTaskFile(taskAllInfo);
                // ??????????????????
                sharedService.pub(SharedType.TASK_STATUS_TILE_ALL_COUNT, String.valueOf(taskAllInfo.getAllRealCount()));
                sharedService.pub(SharedType.TASK_STATUS_TILE_DOWNLOADED_COUNT, "0");
                sharedService.pub(SharedType.TASK_STATUS_PROGRESS, "0%");
                // ????????????
                downloadTask(taskAllInfo);
                return null;
            }

        };
        worker.execute();
    }

    /**
     * ????????????
     */
    public void loadTask(TaskAllInfoEntity taskAllInfo) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                sharedService.pub(SharedType.DOWNLOAD_CONSOLE_CLEAR, true); // ????????????
                sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, "??????????????????...");
                List<Integer> zoomList = new ArrayList<>(taskAllInfo.getEachLayerTask().keySet());
                // ??????????????????
                sharedService.pub(SharedType.TASK_STATUS_CURRENT, "????????????????????????");
                sharedService.pub(SharedType.TASK_STATUS_MAP_TYPE, taskAllInfo.getMapType());
                sharedService.pub(SharedType.TASK_STATUS_LAYERS, zoomList.toString());
                sharedService.pub(SharedType.TASK_STATUS_SAVE_PATH, taskAllInfo.getSavePath());
                sharedService.pub(SharedType.TASK_STATUS_PATH_STYLE, taskAllInfo.getPathStyle());
                if (taskAllInfo.getIsCoverExists()) {
                    sharedService.pub(SharedType.TASK_STATUS_IS_COVER_EXIST, "???");
                } else {
                    sharedService.pub(SharedType.TASK_STATUS_IS_COVER_EXIST, "???");
                }
                switch (taskAllInfo.getImgType()) {
                    case 0 -> sharedService.pub(SharedType.TASK_STATUS_IMG_TYPE, "PNG");
                    case 1 -> sharedService.pub(SharedType.TASK_STATUS_IMG_TYPE, "JPG - ?????????");
                    case 2 -> sharedService.pub(SharedType.TASK_STATUS_IMG_TYPE, "JPG - ????????????");
                    case 3 -> sharedService.pub(SharedType.TASK_STATUS_IMG_TYPE, "JPG - ?????????");
                    default -> {
                    }
                }
                sharedService.pub(SharedType.TASK_STATUS_TILE_ALL_COUNT, String.valueOf(taskAllInfo.getAllRealCount()));
                updateDownloadProcess(new TaskProgressEntity(0, taskAllInfo.getAllRunCount(), (double) taskAllInfo.getAllRunCount() / (double) taskAllInfo.getAllRealCount()));
                // ????????????
                for (TaskInstEntity inst : taskAllInfo.getEachLayerTask().values()) {
                    String log = inst.getZ() + "????????????????????????" + inst.getAllCount() + "???????????????????????????" + inst.getRealCount();
                    sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, log);
                }
                // ????????????
                downloadTask(taskAllInfo);
                return null;
            }

        };
        worker.execute();
    }

    /**
     * ????????????
     */
    public void downloadTask(TaskAllInfoEntity _taskAllInfo) {
        int id = new Random().nextInt(1000000000);
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // ????????????
                TaskAllInfoEntity[] taskAllInfo = new TaskAllInfoEntity[1];
                taskAllInfo[0] = _taskAllInfo;
                TaskState.IS_TASKING = true;
                downloadStart();
                // ??????HTTP
                taskAllInfo[0] = taskStep.setHttpConfig(taskAllInfo[0], (e) -> sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, e));
                // ??????????????????
                sharedService.pub(SharedType.SET_INTERVAL, new IntervalConfig(id, new DownloadMonitoringInterval(taskAllInfo[0], (progress) -> taskSecInterval(taskAllInfo[0], progress)), 1000L));
                // ????????????
                taskStep.tileDownload(taskAllInfo[0], (z) -> {
                    // ????????????????????????
                    sharedService.pub(SharedType.TASK_STATUS_CURRENT, "???????????????" + z + "?????????");
                }, (z) -> {
                    // ???????????????????????? - ??????????????????
                    eachLayerDownloadedTileErrorCallback(taskAllInfo[0]);
                    // ???????????????????????? - ????????????
                    eachLayerDownloadedTileMergeCallback(taskAllInfo[0], z);
                }, (z, name, count, xRun, yRun, success) -> {
                    // ?????????????????????
                    eachTileDownloadedCallback(taskAllInfo[0], z, name, count, xRun, yRun, success);
                }, (e) -> {
                    // ????????????
                    sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, e);
                });
                // ????????????
                TaskState.IS_TASKING = false;
                sharedService.pub(SharedType.CLEAR_INTERVAL, id);
                downloadFinish(taskAllInfo[0]);
                System.gc();
                return null;
            }

        };
        worker.execute();
        downloadWorker = worker;
    }

    /**
     * ??????????????????
     */
    private void taskSecInterval(TaskAllInfoEntity taskAllInfo, TaskProgressEntity progress) {
        // ?????????????????????
        sharedService.pub(SharedType.RESOURCE_USAGE_THREAD_COUNT, String.valueOf(tileDownloadExecutorPool.getActiveCount()));
        sharedService.pub(SharedType.RESOURCE_USAGE_DOWNLOAD_SPEED, downloadAmountInstance.getDiffValue() + "/s");
        sharedService.pub(SharedType.RESOURCE_USAGE_DOWNLOAD_PER_SEC_COUNT, String.valueOf(progress.getCurrentCount() - progress.getLastCount()));
        downloadAmountInstance.saveLast();
        // ??????CPU?????????
        double sysLoad = cpuMonitor.getSystemCpuLoad();
        double proLoad = cpuMonitor.getProcessCpuLoad();
        sharedService.pub(SharedType.CPU_PERCENTAGE_DRAW_SYSTEM, sysLoad);
        sharedService.pub(SharedType.CPU_PERCENTAGE_DRAW_PROCESS, proLoad);
        sharedService.pub(SharedType.RESOURCE_USAGE_SYSTEM_CPU_USAGE, df1.format(sysLoad * 100) + "%");
        sharedService.pub(SharedType.RESOURCE_USAGE_PROCESS_CPU_USAGE, df1.format(proLoad * 100) + "%");
        if (TaskState.IS_TASKING) { // ?????????????????????????????????????????????
            // ??????????????????
            updateDownloadProcess(progress);
        }
        // ??????????????????
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
     * ?????????????????????
     */
    private void eachTileDownloadedCallback(TaskAllInfoEntity taskAllInfo, int z, String name, int count, int xRun, int yRun, boolean success) {
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
     * ???????????????????????? - ??????????????????
     */
    private void eachLayerDownloadedTileErrorCallback(TaskAllInfoEntity taskAllInfo) {
        if (isHandlerCancel) {
            return;
        }
        if (taskAllInfo.getErrorTiles().size() == 0) {
            return;
        }
        taskStep.tileErrorDownload(taskAllInfo, (e) -> sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, e));
    }

    /**
     * ???????????????????????? - ????????????
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
            sharedService.pub(SharedType.TASK_STATUS_CURRENT, "???????????????" + z + "?????????");
            // ??????Mat
            TileMergeMatWrap mat = new TileMergeMatWrap();
            // ???????????????????????????
            sharedService.pub(SharedType.SET_INTERVAL, new IntervalConfig(id, new TileMergeMonitoringInterval(mat, (progress) -> {
                sharedService.pub(SharedType.TILE_MERGE_PROCESS_PIXEL_COUNT, progress.getRunPixel() + "/" + progress.getAllPixel());
                sharedService.pub(SharedType.TILE_MERGE_PROCESS_THREAD, String.valueOf(tileMergeExecutorPool.getActiveCount()));
                sharedService.pub(SharedType.TILE_MERGE_PROCESS_PROGRESS, df2.format(progress.getPerc() * 100) + "%");
            }), 100L));
            // ????????????
            taskStep.mergeTileImage(mat, taskAllInfo, z, (e) -> sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, e), (e) -> {
                sharedService.pub(SharedType.TILE_MERGE_PROCESS_PIXEL_COUNT, mat.getAllPixel() + "/" + mat.getAllPixel());
                sharedService.pub(SharedType.TILE_MERGE_PROCESS_THREAD, "0");
                sharedService.pub(SharedType.TILE_MERGE_PROCESS_PROGRESS, "100.00%");
                // ?????????????????????????????????
                sharedService.pub(SharedType.CLEAR_INTERVAL, id);
            });
            // ????????????true
            taskAllInfo.getEachLayerTask().get(z).setIsMerged(true);
        }
    }

    /**
     * ??????????????????
     */
    private void updateDownloadProcess(TaskProgressEntity progress) {
        sharedService.pub(SharedType.TASK_STATUS_TILE_DOWNLOADED_COUNT, String.valueOf(progress.getCurrentCount()));
        sharedService.pub(SharedType.TASK_STATUS_PROGRESS, df2.format(100 * progress.getPerc()) + "%");
        sharedService.pub(SharedType.DOWNLOAD_CONSOLE_PROGRESS, (int) Math.round(100 * progress.getPerc()));
    }

    /**
     * ????????????
     */
    private void downloadStart() {
        TaskState.IS_TASK_PAUSING = false;
        isHandlerCancel = false;
        sharedService.pub(SharedType.CPU_PERCENTAGE_CLEAR, true);
        sharedService.pub(SharedType.RESOURCE_USAGE_CLEAR, true);
        sharedService.pub(SharedType.TILE_MERGE_PROCESS_CLEAR, true);
        sharedService.pub(SharedType.DOWNLOAD_CONSOLE_PAUSE_BUTTON_STATE, true);
        sharedService.pub(SharedType.DOWNLOAD_CONSOLE_CANCEL_BUTTON_STATE, true);
        sharedService.pub(SharedType.TASK_STATUS_CURRENT, "????????????");
        downloadAmountInstance.reset();
    }

    /**
     * ????????????
     */
    private void downloadFinish(TaskAllInfoEntity taskAllInfo) {
        TaskState.IS_TASK_PAUSING = false;
        downloadWorker = null;
        sharedService.pub(SharedType.RESOURCE_USAGE_CLEAR, true);
        sharedService.pub(SharedType.DOWNLOAD_CONSOLE_PAUSE_BUTTON_TEXT, "????????????");
        sharedService.pub(SharedType.DOWNLOAD_CONSOLE_PAUSE_BUTTON_STATE, false);
        sharedService.pub(SharedType.DOWNLOAD_CONSOLE_CANCEL_BUTTON_STATE, false);
        if (!isHandlerCancel) {
            int allRunCount = 0;
            for (TaskInstEntity inst : taskAllInfo.getEachLayerTask().values()) {
                for (TaskBlockEntity block : inst.getBlocks().values()) {
                    allRunCount = allRunCount + block.getRunCount();
                }
            }
            taskAllInfo.setAllRunCount(allRunCount);
            updateDownloadProcess(new TaskProgressEntity(0, taskAllInfo.getAllRunCount(), (double) taskAllInfo.getAllRunCount() / (double) taskAllInfo.getAllRealCount()));
            sharedService.pub(SharedType.TASK_STATUS_CURRENT, "????????????");
            sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, "????????????");
            System.out.println("[????????????]");
            deleteTaskFile(taskAllInfo);
        } else {
            sharedService.pub(SharedType.TASK_STATUS_CURRENT, "?????????????????????");
            sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, "?????????????????????");
            System.out.println("[?????????????????????]");
        }
    }

    /**
     * ????????????
     */
    public boolean isCancel() {
        return this.isHandlerCancel;
    }

    /**
     * ??????????????????
     */
    public void pauseTask() {
        if (TaskState.IS_TASKING && downloadWorker != null) {
            if (TaskState.IS_TASK_PAUSING) { // ??????
                TaskState.IS_TASK_PAUSING = false;
                sharedService.pub(SharedType.DOWNLOAD_CONSOLE_PAUSE_BUTTON_TEXT, "????????????");
                sharedService.pub(SharedType.TASK_STATUS_CURRENT, "????????????");
                sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, "??????????????????");
            } else { // ??????
                TaskState.IS_TASK_PAUSING = true;
                sharedService.pub(SharedType.DOWNLOAD_CONSOLE_PAUSE_BUTTON_TEXT, "????????????");
                sharedService.pub(SharedType.TASK_STATUS_CURRENT, "????????????");
                sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, "?????????????????????");
            }
        }
    }

    /**
     * ??????????????????
     */
    public void cancelTaks() {
        if (TaskState.IS_TASKING && downloadWorker != null) {
            sharedService.pub(SharedType.DOWNLOAD_CONSOLE_CANCEL_BUTTON_STATE, true);
            sharedService.pub(SharedType.TASK_STATUS_CURRENT, "????????????????????????...");
            sharedService.pub(SharedType.DOWNLOAD_CONSOLE_LOG, "????????????????????????...");
            downloadWorker.cancel(true);
            this.isHandlerCancel = true;
            TaskState.IS_TASKING = false;
        }
    }

    /**
     * ??????????????????
     */
    private void saveTaskFile(TaskAllInfoEntity taskAllInfo) {
        try {
            CommonUtils.saveObj2File(taskAllInfo, taskAllInfo.getSavePath() + "/task_info.jmd");
        } catch (IOException e) {
            log.error("Task File Save Error", e);
        }
    }

    /**
     * ??????????????????
     */
    private void deleteTaskFile(TaskAllInfoEntity taskAllInfo) {
        File file = new File(taskAllInfo.getSavePath() + "/task_info.jmd");
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

}

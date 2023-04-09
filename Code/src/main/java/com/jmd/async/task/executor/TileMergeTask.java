package com.jmd.async.task.executor;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.entity.geo.Polygon;
import com.jmd.entity.result.ImageMergeAsyncTaskResult;
import com.jmd.taskfunc.TileMergeMatWrap;
import com.jmd.util.GeoUtils;
import com.jmd.util.TaskUtils;

@Component
public class TileMergeTask {

    @Async("TileMergeExecutorPool")
    public Future<ImageMergeAsyncTaskResult> exec(
            TileMergeMatWrap mat,
            int z,
            long topLeftX, long topLeftY,
            long xStart, long xEnd,
            long yStart, long yEnd,
            List<Polygon> polygons,
            int imgType, String savePath, String pathStyle,
            int divideXIndex, int divideYIndex
    ) {
        var result = new ImageMergeAsyncTaskResult();
        result.setXStart(xStart);
        result.setXEnd(xEnd);
        result.setYStart(yStart);
        result.setYEnd(yEnd);
        result.setDivideXIndex(divideXIndex);
        result.setDivideYIndex(divideYIndex);
        for (var x = xStart; x <= xEnd; x++) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            for (var y = yStart; y <= yEnd; y++) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                var tile = GeoUtils.getTile(z, x, y);
                var positionX = StaticVar.TILE_WIDTH * (x - topLeftX);
                var positionY = StaticVar.TILE_HEIGHT * (y - topLeftY);
                var filePathAndName = savePath + TaskUtils.getFilePathName(pathStyle, imgType, z, x, y);
                var isInFlag = false;
                for (var polygon : polygons) {
                    isInFlag = GeoUtils.isTileInPolygon(tile, polygon) || GeoUtils.isPolygonInTile(tile, polygon);
                    if (isInFlag) {
                        break;
                    }
                }
                mat.mergeToMat(filePathAndName, positionX, positionY, isInFlag);
            }
        }
        return new AsyncResult<>(result);
    }

}

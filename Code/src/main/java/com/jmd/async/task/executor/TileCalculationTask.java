package com.jmd.async.task.executor;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.jmd.entity.geo.Polygon;
import com.jmd.entity.geo.Tile;
import com.jmd.entity.task.TaskBlockEntity;
import com.jmd.util.GeoUtils;

@Component
public class TileCalculationTask {

    @Async("TileCalculationExecutorPool")
    public Future<TaskBlockEntity> exec(
            int z,
            long x0, long x1,
            long y0, long y1,
            List<Polygon> polygons
    ) {
        var name = z + "-" + x0 + "-" + x1 + "-" + y0 + "-" + y1;
        long count = 0L;
        for (var x = x0; x <= x1; x++) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            for (var y = y0; y <= y1; y++) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                var tile = GeoUtils.getTile(z, x, y);
                for (var polygon : polygons) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    if (GeoUtils.isTileInPolygon(tile, polygon) || GeoUtils.isPolygonInTile(tile, polygon)) {
                        count = count + 1;
                        break;
                    }
                }
            }
        }
        var block = new TaskBlockEntity(name, z, x0, x1, y0, y1, count, x0, y0, 0L);
        return new AsyncResult<>(block);
    }

}

package com.jmd.web.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import com.jmd.ui.common.CommonDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmd.model.controller.WebDownloadSubmitVo;
import com.jmd.model.geo.MercatorPoint;
import com.jmd.model.geo.Polygon;
import com.jmd.ui.frame.download.config.DownloadConfigFrame;
import com.jmd.util.GeoUtils;
import com.jmd.web.service.WebSubmitService;

@Service
public class WebSubmitServiceImpl implements WebSubmitService {

    @Autowired
    private DownloadConfigFrame downloadConfigFrame;

    @Override
    public void blockDownload(WebDownloadSubmitVo vo) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException, InvocationTargetException {
                if (vo.getMapType() == null || vo.getMapType().equals("NOSUPPORT")) {
                    CommonDialog.alert(null, "该瓦片图暂不支持下载");
                    return null;
                }
                if (vo.getPoints().size() == 0) {
                    CommonDialog.alert(null, "未绘制图形");
                    return null;
                }
                List<Polygon> polygons = new ArrayList<>();
                for (List<double[]> each : vo.getPoints()) {
                    List<MercatorPoint> path = new ArrayList<>();
                    for (double[] point : each) {
                        path.add(new MercatorPoint(point[0], point[1]));
                    }
                    polygons.add(new Polygon(path));
                }
                downloadConfigFrame.createNewTask(vo.getTileUrl().get(0), polygons, vo.getTileName(), vo.getMapType());
                return null;
            }
        }.execute();

    }

    @Override
    public void worldDownload(WebDownloadSubmitVo vo) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException, InvocationTargetException {
                if (vo.getMapType() == null || vo.getMapType().equals("NOSUPPORT")) {
                    CommonDialog.alert(null, "该瓦片图暂不支持下载");
                    return null;
                }
                double merc = GeoUtils.MAX_MERC - 1;
                List<double[]> points = new ArrayList<>();
                points.add(new double[]{-merc, merc});
                points.add(new double[]{-merc, -merc});
                points.add(new double[]{merc, -merc});
                points.add(new double[]{merc, merc});
                points.add(new double[]{-merc, merc});
                List<MercatorPoint> mercatorPoints = new ArrayList<>();
                for (double[] point : points) {
                    mercatorPoints.add(new MercatorPoint(point[0], point[1]));
                }
                List<Polygon> polygons = new ArrayList<>();
                polygons.add(new Polygon(mercatorPoints));
                downloadConfigFrame.createNewTask(vo.getTileUrl().get(0), polygons, vo.getTileName(), vo.getMapType());
                return null;
            }
        }.execute();
    }

}

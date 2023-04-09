import { MapBase } from '../map-base';
import { Point } from "../entity/Point";
import { GeoUtil } from "../geo-util";

declare var ol: any;

export class MapWrap {

    /** setFitViewByFeatures */
    public static setFitViewByFeatures(data: {
        map: MapBase,
        features: Array<any>,
        padding: [number, number, number, number],
    }): void {
        if (data.features == null || data.features.length == 0) {
            return;
        }
        let shapes: Array<any> = [];
        for (let i = 0; i < data.features.length; i++) {
            let g = data.features[i].getGeometry();
            if (g != null) {
                shapes.push(g);
            }
        }
        this.setFitViewByShapes({
            map: data.map,
            shapes: shapes,
            padding: data.padding,
        })
    }

    /** setFitViewByShapes */
    public static setFitViewByShapes(data: {
        map: MapBase,
        shapes: Array<any>,
        padding: [number, number, number, number],
    }): void {
        if (data.shapes == null || data.shapes.length == 0) {
            return;
        }
        let points = {
            minS: new Array<Point>(),
            maxS: new Array<Point>(),
        };
        for (let i = 0; i < data.shapes.length; i++) {
            switch (data.shapes[i].constructor) {
                case ol.geom.LineString: {
                    let shape = data.shapes[i];
                    let coord = shape.getCoordinates();
                    let minX = 0, minY = 0, maxX = 0, maxY = 0;
                    for (let n = 0; n < coord.length; n++) {
                        minX = minX ? (minX > coord[n][0] ? coord[n][0] : minX) : coord[n][0];
                        minY = minY ? (minY > coord[n][1] ? coord[n][1] : minY) : coord[n][1];
                        maxX = maxX ? (maxX < coord[n][0] ? coord[n][0] : maxX) : coord[n][0];
                        maxY = maxY ? (maxY < coord[n][1] ? coord[n][1] : maxY) : coord[n][1];
                    }
                    points.minS.push(new Point(minX, minY));
                    points.maxS.push(new Point(maxX, maxY));
                    break;
                }
                case ol.geom.Polygon: {
                    let shape = data.shapes[i];
                    let coords = shape.getCoordinates();
                    for (let h = 0; h < coords.length; h++) {
                        let coord = coords[h];
                        let minX = 0, minY = 0, maxX = 0, maxY = 0;
                        for (let n = 0; n < coord.length; n++) {
                            minX = minX ? (minX > coord[n][0] ? coord[n][0] : minX) : coord[n][0];
                            minY = minY ? (minY > coord[n][1] ? coord[n][1] : minY) : coord[n][1];
                            maxX = maxX ? (maxX < coord[n][0] ? coord[n][0] : maxX) : coord[n][0];
                            maxY = maxY ? (maxY < coord[n][1] ? coord[n][1] : maxY) : coord[n][1];
                        }
                        points.minS.push(new Point(minX, minY));
                        points.maxS.push(new Point(maxX, maxY));
                    }
                    break;
                }
                case ol.geom.Circle: {
                    let shape = data.shapes[i];
                    let m_center = shape.getCenter();
                    let center = GeoUtil.Mercator_To_LngLat(new Point(m_center[0], m_center[1]));
                    let radius = shape.getRadius();
                    let distance = Math.sqrt(2 * Math.pow(radius, 2));
                    let bottomLeft = GeoUtil.destinationVincenty(center, 225, distance);
                    let topRight = GeoUtil.destinationVincenty(center, 45, distance);
                    points.minS.push(GeoUtil.LngLat_To_Mercator(bottomLeft));
                    points.maxS.push(GeoUtil.LngLat_To_Mercator(topRight));
                    break;
                }
                default:
                    break;
            }
        }
        let minX = 0, minY = 0, maxX = 0, maxY = 0;
        for (let i = 0; i < points.minS.length; i++) {
            minX = minX ? (minX > points.minS[i].lng ? points.minS[i].lng : minX) : points.minS[i].lng;
            minY = minY ? (minY > points.minS[i].lat ? points.minS[i].lat : minY) : points.minS[i].lat;
        }
        for (let i = 0; i < points.maxS.length; i++) {
            maxX = maxX ? (maxX < points.maxS[i].lng ? points.maxS[i].lng : maxX) : points.maxS[i].lng;
            maxY = maxY ? (maxY < points.maxS[i].lat ? points.maxS[i].lat : maxY) : points.maxS[i].lat;
        }
        let ext: number[] = [minX, minY, maxX, maxY];
        let view = data.map.getOlMap().getView();
        let size = data.map.getOlMap().getSize();
        if (size != null) {
            if (size[0] < 100) {
                size[0] = 100;
            }
            if (size[1] < 100) {
                size[1] = 100;
            }
        }
        view.fit(ext, {
            nearest: false,
            padding: data.padding,
            size: size,
        })
    }

    /** setFitView */
    public static setFitView(data: {
        map: MapBase,
        extent: {
            bottomLeft: Point,
            topRight: Point
        },
        padding: [number, number, number, number],
    }): void {
        let view = data.map.getOlMap().getView();
        let m_bottomLeft = GeoUtil.LngLat_To_Mercator(data.extent.bottomLeft);
        let m_topRight = GeoUtil.LngLat_To_Mercator(data.extent.topRight);
        let ext: number[] = [m_bottomLeft.lng, m_bottomLeft.lat, m_topRight.lng, m_topRight.lat];
        view.fit(ext, {
            nearest: false,
            padding: data.padding,
        })
    }

    /** 地图添加Overlay */
    public static addOverlay(map: MapBase, overlay: any): void {
        map.getOlMap().addOverlay(overlay);
    }

    /** 地图添加Overlay（多个） */
    public static addOverlays(map: MapBase, overlays: Array<any>): void {
        for (let i = 0; i < overlays.length; i++) {
            map.getOlMap().addOverlay(overlays[i]);
        }
    }

    /** 地图移除Overlay */
    public static removeOverlay(map: MapBase, overlay: any): void {
        map.getOlMap().removeOverlay(overlay);
    }

    /** 地图移除Overlay（多个） */
    public static removeOverlays(map: MapBase, overlays: Array<any>): void {
        for (let i = 0; i < overlays.length; i++) {
            map.getOlMap().removeOverlay(overlays[i]);
        }
    }

    /** 地图清空Overlay */
    public static removeAllOverlays(map: MapBase): void {
        let overlays = map.getOlMap().getOverlays().getArray();
        this.removeOverlays(map, overlays);
    }

    /** 图层添加Feature */
    public static addFeature(map: MapBase, name: string, feature: any): void {
        let source = map.getLayerByName(name).getSource();
        if (!source.hasFeature(feature)) {
            source.addFeature(feature);
        }
    }

    /** 图层添加Feature（多个） */
    public static addFeatures(map: MapBase, name: string, features: Array<any>): void {
        let source = map.getLayerByName(name).getSource();
        for (let i = 0; i < features.length; i++) {
            if (!source.hasFeature(features[i])) {
                source.addFeature(features[i]);
            }
        }
    }

    /** 图层移除Feature */
    public static removeFeature(map: MapBase, name: string, feature: any): void {
        let source = map.getLayerByName(name).getSource();
        if (source.hasFeature(feature)) {
            source.removeFeature(feature);
        }
    }

    /** 图层移除Feature（多个） */
    public static removeFeatures(map: MapBase, name: string, features: Array<any>): void {
        let source = map.getLayerByName(name).getSource();
        for (let i = 0; i < features.length; i++) {
            if (source.hasFeature(features[i])) {
                source.removeFeature(features[i]);
            }
        }
    }

    /** 图层清空Feature */
    public static removeAllFeatures(map: MapBase, name: string): void {
        let source = map.getLayerByName(name).getSource();
        source.clear();
    }

}

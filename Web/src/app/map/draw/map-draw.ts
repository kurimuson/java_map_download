import { polygonFillColor, polygonStrokeColor } from '../map-base';
import { Point } from "../entity/Point";

declare var ol: any;

export class MapDraw {

	/** 新建多边形Feature */
	public static createPolygonFeature(points: Array<Point>): any {
		let pts = [];
		for (let i = 0; i < points.length; i++) {
			pts.push(ol.proj.fromLonLat([points[i].lng, points[i].lat]));
		}
		let polygon = new ol.geom.Polygon([pts]);
		let feature = new ol.Feature({ geometry: polygon });
		let style = new ol.style.Style({
			stroke: new ol.style.Stroke({
				color: polygonStrokeColor,
				width: 3
			}),
			fill: new ol.style.Fill({
				color: polygonFillColor,
			}),
		});
		feature.setStyle(style);
		return feature;
	}

}

package com.jmd.util;

import java.util.ArrayList;
import java.util.List;

import com.jmd.model.geo.Bound;
import com.jmd.model.geo.LngLatPoint;
import com.jmd.model.geo.MercatorPoint;
import com.jmd.model.geo.Polygon;
import com.jmd.model.geo.Tile;

public class GeoUtils {

	// 卫星椭球坐标投影到平面坐标系的投影因子
	private static final double A = 6378245.0;
	// 椭球的偏心率
	private static final double EE = 0.00669342162296594323;
	// 容差
	private static final double PRECISION = 2e-10; // 默认2e-10

	/**
	 * MAX_MERC = 20037508.3427892;<br>
	 * minMerc = -20037508.3427892;<br>
	 * 经纬度为[0,0]时墨卡托坐标为[0,0]<br>
	 * MAX_MERC对应纬度180<br>
	 * MIN_MERC对应纬度-180<br>
	 * 墨卡托投影图左上角,即[x:0,y:0]左上角,坐标为[minMerc,MAX_MERC]<br>
	 * 墨卡托投影图右上角,即[x:m,y:0]右上角,坐标为[MAX_MERC,MAX_MERC]<br>
	 * 墨卡托投影图左下角,即[x:0,y:m]左下角,坐标为[minMerc,minMerc]<br>
	 * 墨卡托投影图右下角,即[x:m,y:m]右下角,坐标为[MAX_MERC,minMerc]<br>
	 * [-20037508,20037508]----------------[20037508,20037508]<br>
	 * -------------------------------------------------------<br>
	 * -------------------------[0,0]-------------------------<br>
	 * -------------------------------------------------------<br>
	 * [-20037508,-20037508]--------------[20037508,-20037508]<br>
	 * 为了方便计算，算法中使用如下坐标系<br>
	 * [0,0]--------------------------------------[40075016,0]<br>
	 * -------------------------------------------------------<br>
	 * ------------------[20037508,20037508]------------------<br>
	 * -------------------------------------------------------<br>
	 * [0,40075016]------------------------[40075016,40075016]<br>
	 * 自定义坐标系转TSM坐标系方法:<br>
	 * Ty = MAX_MERC - y<br>
	 * Tx = x - MAX_MERC<br>
	 * TSM坐标系转自定义坐标系方法:<br>
	 * y = MAX_MERC - Ty<br>
	 * x = Tx + MAX_MERC<br>
	 */
	public static final double MAX_MERC = 20037508.3427892;

	/** 经纬度转墨卡托 */
	public static MercatorPoint LngLat2Mercator(LngLatPoint point) {
		double x = point.getLng() * MAX_MERC / 180;
		double y = Math.log(Math.tan((90 + point.getLat()) * Math.PI / 360)) / (Math.PI / 180);
		y = y * MAX_MERC / 180;
		return new MercatorPoint(x, y);
	}

	/** 墨卡托转经纬度 */
	public static LngLatPoint Mercator2LngLat(MercatorPoint point) {
		double x = point.getLng() / MAX_MERC * 180;
		double y = point.getLat() / MAX_MERC * 180;
		y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
		return new LngLatPoint(x, y);
	}

	/** 通过XYZ获取Tile实例 */
	public static Tile getTile(int z, long x, long y) {
		Tile tile = new Tile();
		tile.setZ(z);
		tile.setX(x);
		tile.setY(y);
		int count = (int) Math.pow(2, z);
		double each = MAX_MERC / ((double) count / 2);
		double each_x = each * x;
		double each_x_1 = each * (x + 1);
		double each_y = each * y;
		double each_y_1 = each * (y + 1);
		tile.setTopLeft(new MercatorPoint(each_x - MAX_MERC, MAX_MERC - each_y));
		tile.setTopRight(new MercatorPoint(each_x_1 - MAX_MERC, MAX_MERC - each_y));
		tile.setBottomLeft(new MercatorPoint(each_x - MAX_MERC, MAX_MERC - each_y_1));
		tile.setBottomRight(new MercatorPoint(each_x_1 - MAX_MERC, MAX_MERC - each_y_1));
		return tile;
	}

	/** 通过墨卡托坐标获取Tile实例 */
	public static Tile getTile(int zoom, MercatorPoint point) {
		double cx = point.getLng() + MAX_MERC;
		double cy = MAX_MERC - point.getLat();
		int count = (int) Math.pow(2, zoom);
		double each = MAX_MERC / ((double) count / 2);
		int count_x = (int) Math.floor(cx / each);
		int count_y = (int) Math.floor(cy / each);
		Tile tile = getTile(zoom, count_x, count_y);
		return tile;
	}

	/** 通过经纬度坐标获取Tile实例 */
	public static Tile getTile(int zoom, LngLatPoint point) {
		return getTile(zoom, LngLat2Mercator(point));
	}

	/** 获取多边形bounds */
	public static Bound getPolygonBound(Polygon polygon) {
		Bound bound = new Bound();
		Double minX = null, minY = null, maxX = null, maxY = null;
		for (MercatorPoint point : polygon.getPath()) {
			minX = minX != null ? (minX > point.getLng() ? point.getLng() : minX) : point.getLng();
			minY = minY != null ? (minY > point.getLat() ? point.getLat() : minY) : point.getLat();
			maxX = maxX != null ? (maxX < point.getLng() ? point.getLng() : maxX) : point.getLng();
			maxY = maxY != null ? (maxY < point.getLat() ? point.getLat() : maxY) : point.getLat();
		}
		bound.setTopLeft(new MercatorPoint(minX, maxY));
		bound.setTopRight(new MercatorPoint(maxX, maxY));
		bound.setBottomLeft(new MercatorPoint(minX, minY));
		bound.setBottomRight(new MercatorPoint(maxX, minY));
		return bound;
	}

	/** 获取多边形bounds（多个） */
	public static Bound getPolygonsBound(List<Polygon> polygons) {
		Bound bound = new Bound();
		Double minX = null, minY = null, maxX = null, maxY = null;
		for (Polygon polygon : polygons) {
			for (MercatorPoint point : polygon.getPath()) {
				minX = minX != null ? (minX > point.getLng() ? point.getLng() : minX) : point.getLng();
				minY = minY != null ? (minY > point.getLat() ? point.getLat() : minY) : point.getLat();
				maxX = maxX != null ? (maxX < point.getLng() ? point.getLng() : maxX) : point.getLng();
				maxY = maxY != null ? (maxY < point.getLat() ? point.getLat() : maxY) : point.getLat();
			}
		}
		bound.setTopLeft(new MercatorPoint(minX, maxY));
		bound.setTopRight(new MercatorPoint(maxX, maxY));
		bound.setBottomLeft(new MercatorPoint(minX, minY));
		bound.setBottomRight(new MercatorPoint(maxX, minY));
		return bound;
	}

	/** 判断瓦片图是否在多边形内 */
	public static boolean isTileInPolygon(Tile tile, Polygon polygon) {
		return isPointInPolygon(tile.getTopLeft(), polygon) || isPointInPolygon(tile.getTopRight(), polygon)
				|| isPointInPolygon(tile.getBottomLeft(), polygon) || isPointInPolygon(tile.getBottomRight(), polygon);
	}

	/** 判断多边形是否在瓦片图区块内 */
	public static boolean isPolygonInTile(Tile tile, Polygon polygon) {
		ArrayList<MercatorPoint> path = new ArrayList<MercatorPoint>();
		path.add(tile.getTopLeft());
		path.add(tile.getTopRight());
		path.add(tile.getBottomRight());
		path.add(tile.getBottomLeft());
		Polygon tilePolygon = new Polygon(path);
		for (MercatorPoint point : polygon.getPath()) {
			if (isPointInPolygon(point, tilePolygon)) {
				return true;
			}
		}
		return false;
	}

	/** 判断点是否在多边形内 */
	/** 提取自百度地图API */
	public static boolean isPointInPolygon(MercatorPoint markerPoint, Polygon polygon) {
		// 下述代码来源：http://paulbourke.net/geometry/insidepoly/，进行了部分修改
		// 基本思想是利用射线法，计算射线与多边形各边的交点，如果是偶数，则点在多边形外，否则
		// 在多边形内。还会考虑一些特殊情况，如点在多边形顶点上，点在多边形边上等特殊情况。
		int N = polygon.getPath().size();
		boolean boundOrVertex = true; // 如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
		int intersectCount = 0; // cross points count of x
		double precision = PRECISION; // 浮点类型计算时候与0比较时候的容差
		MercatorPoint p1, p2; // neighbour bound vertices
		MercatorPoint p = markerPoint; // 测试点
		p1 = polygon.getPath().get(0); // left vertex
		for (int i = 1; i <= N; ++i) { // check all rays
			if (p.equals(p1)) {
				return boundOrVertex; // p is an vertex
			}
			p2 = polygon.getPath().get(i % N); // right vertex
			// ray is outside of our interests
			if (p.getLat() < Math.min(p1.getLat(), p2.getLat()) || p.getLat() > Math.max(p1.getLat(), p2.getLat())) {
				p1 = p2;
				continue; // next ray left point
			}
			// ray is crossing over by the algorithm (common part of)
			if (p.getLat() > Math.min(p1.getLat(), p2.getLat()) && p.getLat() < Math.max(p1.getLat(), p2.getLat())) {
				// x is before of ray
				if (p.getLng() <= Math.max(p1.getLng(), p2.getLng())) {
					// overlies on a horizontal ray
					if (p1.getLat() == p2.getLat() && p.getLng() >= Math.min(p1.getLng(), p2.getLng())) {
						return boundOrVertex;
					}
					if (p1.getLng() == p2.getLng()) { // ray is vertical
						if (p1.getLng() == p.getLng()) { // overlies on a vertical ray
							return boundOrVertex;
						} else { // before ray
							++intersectCount;
						}
					} else { // cross point on the left side
						// cross point of lng
						double xinters = (p.getLat() - p1.getLat()) * (p2.getLng() - p1.getLng())
								/ (p2.getLat() - p1.getLat()) + p1.getLng();
						// overlies on a ray
						if (Math.abs(p.getLng() - xinters) < precision) {
							return boundOrVertex;
						}
						if (p.getLng() < xinters) { // before ray
							++intersectCount;
						}
					}
				}
			} else { // special case when ray is crossing through the vertex
				if (p.getLat() == p2.getLat() && p.getLng() <= p2.getLng()) { // p crossing over p2
					MercatorPoint p3 = polygon.getPath().get((i + 1) % N); // next vertex
					if (p.getLat() >= Math.min(p1.getLat(), p3.getLat())
							&& p.getLat() <= Math.max(p1.getLat(), p3.getLat())) {
						// p.lat lies between p1.lat & p3.lat
						++intersectCount;
					} else {
						intersectCount += 2;
					}
				}
			}
			p1 = p2; // next ray left point
		}
		if (intersectCount % 2 == 0) { // 偶数在多边形外
			return false;
		} else { // 奇数在多边形内
			return true;
		}
	}

	/** XYZ转必应坐标 */
	public static String xyz2Bing(int _z, long _x, long _y) {
		StringBuffer result = new StringBuffer();
		double x = _x + 1;
		double y = _y + 1;
		int z_all = (int) Math.pow(2, _z);
		for (int i = 1; i <= _z; i++) {
			double z0 = z_all / Math.pow(2, i - 1);
			// 左上
			if (x / z0 <= 0.5 && y / z0 <= 0.5) {
				result.append("0");
			}
			// 右上
			if (x / z0 > 0.5 && y / z0 <= 0.5) {
				result.append("1");
				x = x - z0 / 2;
			}
			// 左下
			if (x / z0 <= 0.5 && y / z0 > 0.5) {
				result.append("2");
				y = y - z0 / 2;
			}
			// 右下
			if (x / z0 > 0.5 && y / z0 > 0.5) {
				result.append("3");
				x = x - z0 / 2;
				y = y - z0 / 2;
			}
		}
		return result.toString();
	}

	/** 是否超出中国范围 */
	private static boolean outOfChina(LngLatPoint pt) {
		double lat = +pt.getLat();
		double lng = +pt.getLng();
		// 纬度3.86~53.55,经度73.66~135.05
		return !(lng > 73.66 && lng < 135.05 && lat > 3.86 && lat < 53.55);
	}

	/** WGS84坐标转GCJ02坐标 */
	public static LngLatPoint wgs84_To_gcj02(LngLatPoint pt) {
		double lng = pt.getLng();
		double lat = pt.getLat();
		if (outOfChina(pt)) {
			return pt;
		} else {
			double dLat = transformLat(lng - 105.0, lat - 35.0);
			double dLng = transformLng(lng - 105.0, lat - 35.0);
			double radLat = lat / 180.0 * Math.PI;
			double magic = Math.sin(radLat);
			magic = 1 - EE * magic * magic;
			double sqrtMagic = Math.sqrt(magic);
			dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * Math.PI);
			dLng = (dLng * 180.0) / (A / sqrtMagic * Math.cos(radLat) * Math.PI);
			double mgLat = lat + dLat;
			double mgLng = lng + dLng;
			return new LngLatPoint(mgLng, mgLat);
		}
	}

	/** GCJ02坐标转WGS84坐标 */
	public static LngLatPoint gcj02_To_wgs84(LngLatPoint pt) {
		double lng = pt.getLng();
		double lat = pt.getLat();
		if (outOfChina(pt)) {
			return pt;
		} else {
			double dlat = transformLat(lng - 105.0, lat - 35.0);
			double dlng = transformLng(lng - 105.0, lat - 35.0);
			double radlat = lat / 180.0 * Math.PI;
			double magic = Math.sin(radlat);
			magic = 1 - EE * magic * magic;
			double sqrtmagic = Math.sqrt(magic);
			dlat = (dlat * 180.0) / ((A * (1 - EE)) / (magic * sqrtmagic) * Math.PI);
			dlng = (dlng * 180.0) / (A / sqrtmagic * Math.cos(radlat) * Math.PI);
			double mglat = lat + dlat;
			double mglng = lng + dlng;
			return new LngLatPoint(lng * 2 - mglng, lat * 2 - mglat);
		}
	}

	private static double transformLat(double lat, double lng) {
		double ret = -100.0 + 2.0 * lat + 3.0 * lng + 0.2 * lng * lng + 0.1 * lat * lng
				+ 0.2 * Math.sqrt(Math.abs(lat));
		ret += (20.0 * Math.sin(6.0 * lat * Math.PI) + 20.0 * Math.sin(2.0 * lat * Math.PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lng * Math.PI) + 40.0 * Math.sin(lng / 3.0 * Math.PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(lng / 12.0 * Math.PI) + 320 * Math.sin(lng * Math.PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transformLng(double lat, double lng) {
		double ret = 300.0 + lat + 2.0 * lng + 0.1 * lat * lat + 0.1 * lat * lng + 0.1 * Math.sqrt(Math.abs(lat));
		ret += (20.0 * Math.sin(6.0 * lat * Math.PI) + 20.0 * Math.sin(2.0 * lat * Math.PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lat * Math.PI) + 40.0 * Math.sin(lat / 3.0 * Math.PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(lat / 12.0 * Math.PI) + 300.0 * Math.sin(lat / 30.0 * Math.PI)) * 2.0 / 3.0;
		return ret;
	}

}

import { Point } from './entity/Point';
import { PointEN } from './entity/PointEN';
import { PointENObj } from './entity/PointENObj';
import { CommonUtil } from "../util/common-util";

export class GeoUtil {

	private static x_PI: number = 3.14159265358979324 * 3000.0 / 180.0;
	private static PI: number = 3.1415926535897932384626;
	private static a: number = 6378245.0;
	private static ee: number = 0.00669342162296594323;

	public static EARTH_RADIUS: number = GeoUtil.a;

	// 容差
	private static PRECISION = 0.00005; // 默认2e-10

	/**
	 * js小数取整方法（去掉小数点，不四舍五入）：
	 * 1、~~1.8
	 * 2、1.8>>0
	 * 3、Math.floor(1.8)
	 */

	public static getCirclePoints(center: Point, radius: number): Array<Point> {
		let num = 72;
		let cphase = 2 * Math.PI / num;
		let pts = [];
		for (let k = 0; k < num; k++) {
			let dx = (radius * Math.cos(k * cphase));
			let dy = (radius * Math.sin(k * cphase));
			let dlng = dx / (GeoUtil.EARTH_RADIUS * Math.cos(center.lat * Math.PI / 180) * Math.PI / 180);
			let dlat = dy / (GeoUtil.EARTH_RADIUS * Math.PI / 180);
			let newlng = center.lng + dlng;
			let newlat = center.lat + dlat;
			pts.push(new Point(newlng, newlat));
		}
		return pts;
	}

	public static ensString2EnPointObj(s: string): PointENObj {
		let pointEN: PointEN;
		if (s.indexOf("E") == 0) {
			pointEN = new PointEN(s.split(",")[0], s.split(",")[1]);
		} else {
			pointEN = new PointEN(s.split(",")[1], s.split(",")[0]);
		}
		if (pointEN.lng == null || pointEN.lng == "" || pointEN.lat == null || pointEN.lat == "") {
			return new PointENObj({ h: 0, m: 0, s: 0 }, { h: 0, m: 0, s: 0 });
		}
		let lng: string = pointEN.lng.split("E")[1];
		let h_lng: number = Number(lng.split("°")[0]);
		let m_lng: number = Number(lng.split("°")[1].split("′")[0]);
		let s_lng: number = Number(lng.split("°")[1].split("′")[1].split("″")[0]);
		let lat: string = pointEN.lat.split("N")[1];
		let h_lat: number = Number(lat.split("°")[0]);
		let m_lat: number = Number(lat.split("°")[1].split("′")[0]);
		let s_lat: number = Number(lat.split("°")[1].split("′")[1].split("″")[0]);
		let pointObj = new PointENObj(
			{ h: h_lng, m: m_lng, s: Number(s_lng) },
			{ h: h_lat, m: m_lat, s: Number(s_lat) },
		);
		if (pointObj.valid()) {
			return pointObj;
		} else {
			return new PointENObj({ h: 0, m: 0, s: 0 }, { h: 0, m: 0, s: 0 });
		}
	}

	public static lnglat2ENPoint(point: Point): PointEN {
		let e_h1: number = ~~point.lng;
		let e_m1: number = ~~((point.lng - e_h1) * 60);
		let e_s1: number = (((point.lng - e_h1) * 60) - e_m1) * 60;
		let e_s1_num: number = CommonUtil.round(e_s1, 1);
		if (e_s1_num >= 60) {
			e_m1 = e_m1 + 1;
			e_s1_num = e_s1_num - 60;
		}
		if (e_m1 >= 60) {
			e_h1 = e_h1 + 1;
			e_m1 = e_m1 - 60;
		}
		let e: string = "E" + e_h1 + "°" + e_m1 + "′" + e_s1_num + "″";
		let n_h1: number = ~~point.lat;
		let n_m1: number = ~~((point.lat - n_h1) * 60);
		let n_s1: number = ((((point.lat - n_h1) * 60) - n_m1) * 60);
		let n_s1_num: number = CommonUtil.round(n_s1, 1);
		if (n_s1_num >= 60) {
			n_m1 = n_m1 + 1;
			n_s1_num = n_s1_num - 60;
		}
		if (n_m1 >= 60) {
			n_h1 = n_h1 + 1;
			n_m1 = n_m1 - 60;
		}
		let n: string = "N" + n_h1 + "°" + n_m1 + "′" + n_s1_num + "″";
		return new PointEN(e, n);
	}

	public static lnglat2ENPointFix0(point: Point): PointEN {
		let e_h1: number = ~~point.lng;
		let e_m1: number = ~~((point.lng - e_h1) * 60);
		let e_s1: number = (((point.lng - e_h1) * 60) - e_m1) * 60;
		let e_s1_num: number = CommonUtil.round(e_s1, 1);
		if (e_s1_num >= 60) {
			e_m1 = e_m1 + 1;
			e_s1_num = e_s1_num - 60;
		}
		if (e_m1 >= 60) {
			e_h1 = e_h1 + 1;
			e_m1 = e_m1 - 60;
		}
		let e_m1_fix0: string;
		if (e_m1 < 10) {
			e_m1_fix0 = "0" + e_m1;
		} else {
			e_m1_fix0 = "" + e_m1;
		}
		let e_s1_fix0: string;
		if (e_s1_num < 10) {
			e_s1_fix0 = "0" + e_s1_num.toFixed(1);
		} else {
			e_s1_fix0 = e_s1_num.toFixed(1);
		}
		let e: string = "E" + e_h1 + "°" + e_m1_fix0 + "′" + e_s1_fix0 + "″";
		let n_h1: number = ~~point.lat;
		let n_m1: number = ~~((point.lat - n_h1) * 60);
		let n_s1: number = (((point.lat - n_h1) * 60) - n_m1) * 60;
		let n_s1_num: number = CommonUtil.round(n_s1, 1);
		if (n_s1_num >= 60) {
			n_m1 = n_m1 + 1;
			n_s1_num = n_s1_num - 60;
		}
		if (n_m1 >= 60) {
			n_h1 = n_h1 + 1;
			n_m1 = n_m1 - 60;
		}
		let n_m1_fix0: string;
		if (n_m1 < 10) {
			n_m1_fix0 = "0" + n_m1;
		} else {
			n_m1_fix0 = "" + n_m1;
		}
		let n_s1_fix0: string;
		if (n_s1_num < 10) {
			n_s1_fix0 = "0" + n_s1_num.toFixed(1);
		} else {
			n_s1_fix0 = n_s1_num.toFixed(1);
		}
		let n: string = "N" + n_h1 + "°" + n_m1_fix0 + "′" + n_s1_fix0 + "″";
		return new PointEN(e, n);
	}

	public static lnglat2ENPointObj(point: Point): PointENObj {
		let e_h1: number = ~~point.lng;
		let e_m1: number = ~~((point.lng - e_h1) * 60);
		let e_s1: number = (((point.lng - e_h1) * 60) - e_m1) * 60;
		let e_s1_num: number = CommonUtil.round(e_s1, 3);
		if (e_s1_num >= 60) {
			e_m1 = e_m1 + 1;
			e_s1_num = e_s1_num - 60;
		}
		if (e_m1 >= 60) {
			e_h1 = e_h1 + 1;
			e_m1 = e_m1 - 60;
		}
		let n_h1: number = ~~point.lat;
		let n_m1: number = ~~((point.lat - n_h1) * 60);
		let n_s1: number = (((point.lat - n_h1) * 60) - n_m1) * 60;
		let n_s1_num: number = CommonUtil.round(n_s1, 3);
		if (n_s1_num >= 60) {
			n_m1 = n_m1 + 1;
			n_s1_num = n_s1_num - 60;
		}
		if (n_m1 >= 60) {
			n_h1 = n_h1 + 1;
			n_m1 = n_m1 - 60;
		}
		return new PointENObj(
			{ h: e_h1, m: e_m1, s: Number(e_s1_num) },
			{ h: n_h1, m: n_m1, s: Number(n_s1_num) }
		);
	}

	public static ENPoint2lnglat(pointEN: PointEN) {
		let lng: string = pointEN.lng.indexOf("E") == -1 ? pointEN.lng.split("E")[0] : pointEN.lng.split("E")[1];
		let h_lng: number = parseInt(lng.split("°")[0]);
		let m_lng: number = parseFloat(lng.split("°")[1].split("′")[0]) / 60;
		let s_lng: number = parseFloat(lng.split("°")[1].split("′")[1].split("″")[0]) / 3600;
		let out_lng: number = parseFloat((h_lng + m_lng + s_lng).toFixed(6));
		let lat: string = pointEN.lat.indexOf("N") == -1 ? pointEN.lat.split("N")[0] : pointEN.lat.split("N")[1];
		let h_lat: number = parseInt(lat.split("°")[0]);
		let m_lat: number = parseFloat(lat.split("°")[1].split("′")[0]) / 60;
		let s_lat: number = parseFloat(lat.split("°")[1].split("′")[1].split("″")[0]) / 3600;
		let out_lat: number = parseFloat((h_lat + m_lat + s_lat).toFixed(6));
		return new Point(out_lng, out_lat);
	}

	public static LngLat_To_Mercator(point: Point): Point {
		let x = point.lng * 20037508.34 / 180;
		let y = Math.log(Math.tan((90 + point.lat) * Math.PI / 360)) / (Math.PI / 180);
		y = y * 20037508.34 / 180;
		return new Point(x, y);
	}

	public static Mercator_To_LngLat(point: Point): Point {
		let x = point.lng / 20037508.34 * 180;
		let y = point.lat / 20037508.34 * 180;
		y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
		return new Point(x, y);
	}

	public static wgs84_To_gcj02(point: Point): Point {
		let lat = +point.lat;
		let lng = +point.lng;
		if (this.outOfChina(point)) {
			return point;
		} else {
			let dlat = this.transformLat(new Point(lng - 105.0, lat - 35.0));
			let dlng = this.transformLng(new Point(lng - 105.0, lat - 35.0));
			let radlat = lat / 180.0 * this.PI;
			let magic = Math.sin(radlat);
			magic = 1 - this.ee * magic * magic;
			let sqrtmagic = Math.sqrt(magic);
			dlat = (dlat * 180.0) / ((this.a * (1 - this.ee)) / (magic * sqrtmagic) * this.PI);
			dlng = (dlng * 180.0) / (this.a / sqrtmagic * Math.cos(radlat) * this.PI);
			let mglat = lat + dlat;
			let mglng = lng + dlng;
			return new Point(mglng, mglat);
		}
	}

	public static gcj02_To_wgs84(point: Point): Point {
		let lat = +point.lat;
		let lng = +point.lng;
		if (this.outOfChina(point)) {
			return point;
		} else {
			let dlat = this.transformLat(new Point(lng - 105.0, lat - 35.0));
			let dlng = this.transformLng(new Point(lng - 105.0, lat - 35.0));
			let radlat = lat / 180.0 * this.PI;
			let magic = Math.sin(radlat);
			magic = 1 - this.ee * magic * magic;
			let sqrtmagic = Math.sqrt(magic);
			dlat = (dlat * 180.0) / ((this.a * (1 - this.ee)) / (magic * sqrtmagic) * this.PI);
			dlng = (dlng * 180.0) / (this.a / sqrtmagic * Math.cos(radlat) * this.PI);
			let mglat = lat + dlat;
			let mglng = lng + dlng;
			return new Point(lng * 2 - mglng, lat * 2 - mglat);
		}
	}

	private static transformLat(point: Point): number {
		let lat = +point.lat;
		let lng = +point.lng;
		let ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
		ret += (20.0 * Math.sin(6.0 * lng * this.PI) + 20.0 * Math.sin(2.0 * lng * this.PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lat * this.PI) + 40.0 * Math.sin(lat / 3.0 * this.PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(lat / 12.0 * this.PI) + 320 * Math.sin(lat * this.PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static transformLng(point: Point): number {
		let lat = +point.lat;
		let lng = +point.lng;
		let ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
		ret += (20.0 * Math.sin(6.0 * lng * this.PI) + 20.0 * Math.sin(2.0 * lng * this.PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lng * this.PI) + 40.0 * Math.sin(lng / 3.0 * this.PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(lng / 12.0 * this.PI) + 300.0 * Math.sin(lng / 30.0 * this.PI)) * 2.0 / 3.0;
		return ret;
	}

	private static outOfChina(point: Point): boolean {
		let lat = +point.lat;
		let lng = +point.lng;
		// 纬度3.86~53.55,经度73.66~135.05
		return !(lng > 73.66 && lng < 135.05 && lat > 3.86 && lat < 53.55);
	}


	/** 两点之间的距离 */
	public static getDistance(point1: Point, point2: Point): number {
		point1.lng = this.getLoop(point1.lng, -180, 180);
		point1.lat = this.getRange(point1.lat, -74, 74);
		point2.lng = this.getLoop(point2.lng, -180, 180);
		point2.lat = this.getRange(point2.lat, -74, 74);
		let x1, x2, y1, y2;
		x1 = this.degToRad(point1.lng);
		y1 = this.degToRad(point1.lat);
		x2 = this.degToRad(point2.lng);
		y2 = this.degToRad(point2.lat);
		return this.a * Math.acos((Math.sin(y1) * Math.sin(y2) + Math.cos(y1) * Math.cos(y2) * Math.cos(x2 - x1)));
	}

	/** 将度转化为弧度 */
	public static degToRad(deg: number): number {
		return Math.PI * deg / 180;
	}

	/** 将弧度转化为度 */
	public static radToDeg(rad: number): number {
		return rad * 180 / Math.PI;
	}

	/** 将v值限定在a,b之间，纬度使用 */
	private static getRange(v: number, a: number, b: number): number {
		if (a != null) {
			v = Math.max(v, a);
		}
		if (b != null) {
			v = Math.min(v, b);
		}
		return v;
	}


	/** 将v值限定在a,b之间，经度使用 */
	private static getLoop(v: number, a: number, b: number): number {
		while (v > b) {
			v -= b - a;
		}
		while (v < a) {
			v += b - a;
		}
		return v;
	}

	/** 获取扇形圆弧 */
	public static getCircularArcPoints(angleA: number, angleB: number, center: Point, radius: number): Array<Point> {
		let flabellateRadius = radius; // 扇形半径
		let flabellateRadiusPointnNum = 3;
		let flabellateStartDegree = angleA;
		let flabellateEndDegree = angleB;
		let flabellatePts = new Array<Point>();
		for (let i = flabellateStartDegree; i < flabellateEndDegree + 0.001; i += flabellateRadiusPointnNum) {
			let _point = this.EOffsetBearing(center, flabellateRadius, 90 - i)
			flabellatePts.push(new Point(_point.lng, _point.lat));
		}
		return flabellatePts;
	}

	/** 使用数学的方法计算需要画扇形的圆弧上的点坐标 */
	public static EOffsetBearing(point: Point, dist: number, bearing: number): Point {
		let lngConv = this.getDistance(point, new Point(point.lng + 0.1, point.lat)) * 10; //计算1经度与原点的距离
		let latConv = this.getDistance(point, new Point(point.lng, point.lat + 0.1)) * 10; //计算1纬度与原点的距离
		let lat = dist * Math.sin(bearing * Math.PI / 180) / latConv; //正弦计算待获取的点的纬度与原点纬度差
		let lng = dist * Math.cos(bearing * Math.PI / 180) / lngConv; //余弦计算待获取的点的经度与原点经度差
		return new Point(point.lng + lng, point.lat + lat);
	}

	/** 计算点是否在多边形内 */
	public static isPointInPolygon(markerPoint: Point, polygonPoints: Array<Point>): boolean {
		// 下述代码来源：http://paulbourke.net/geometry/insidepoly/，进行了部分修改
		// 基本思想是利用射线法，计算射线与多边形各边的交点，如果是偶数，则点在多边形外，否则
		// 在多边形内。还会考虑一些特殊情况，如点在多边形顶点上，点在多边形边上等特殊情况。
		let pts = polygonPoints;
		let N = polygonPoints.length;
		let boundOrVertex = true; // 如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
		let intersectCount = 0; // cross points count of x
		let precision = this.PRECISION; // 浮点类型计算时候与0比较时候的容差
		let p1: Point, p2: Point; // neighbour bound vertices
		let p = markerPoint; // 测试点
		p1 = pts[0]; // left vertex
		for (let i = 0; i < polygonPoints.length; i++) {
			if (Math.abs(p.lng - pts[i].lng) <= precision && Math.abs(p.lat - pts[i].lat) <= precision) {
				return boundOrVertex; // p is an vertex
			}
		}
		for (let i = 1; i <= N; ++i) { // check all rayslet sa = false;
			// if (p.equals(p1)) {
			//     return boundOrVertex; // p is an vertex
			// }
			p2 = pts[i % N]; // right vertex
			// ray is outside of our interests
			if (p.lat < Math.min(p1.lat, p2.lat) || p.lat > Math.max(p1.lat, p2.lat)) {
				p1 = p2;
				continue; // next ray left point
			}
			// ray is crossing over by the algorithm (common part of)
			if (p.lat > Math.min(p1.lat, p2.lat) && p.lat < Math.max(p1.lat, p2.lat)) {
				// x is before of ray
				if (p.lng <= Math.max(p1.lng, p2.lng)) {
					// overlies on a horizontal ray
					if (p1.lat == p2.lat && p.lng >= Math.min(p1.lng, p2.lng)) {
						return boundOrVertex;
					}
					if (p1.lng == p2.lng) { // ray is vertical
						if (p1.lng == p.lng) { // overlies on a vertical ray
							return boundOrVertex;
						} else { // before ray
							++intersectCount;
						}
					} else { // cross point on the left side
						// cross point of lng
						let xinters = (p.lat - p1.lat) * (p2.lng - p1.lng)
							/ (p2.lat - p1.lat) + p1.lng;
						// overlies on a ray
						if (Math.abs(p.lng - xinters) < precision) {
							return boundOrVertex;
						}
						if (p.lng < xinters) { // before ray
							++intersectCount;
						}
					}
				}
			} else { // special case when ray is crossing through the vertex
				if (p.lat == p2.lat && p.lng <= p2.lng) { // p crossing over p2
					let p3: Point = pts[(i + 1) % N]; // next vertex
					if (p.lat >= Math.min(p1.lat, p3.lat)
						&& p.lat <= Math.max(p1.lat, p3.lat)) {
						// p.lat lies between p1.lat & p3.lat
						++intersectCount;
					} else {
						intersectCount += 2;
					}
				}
			}
			p1 = p2; // next ray left point
		}
		return intersectCount % 2 != 0;
	}

	/** 根据某一点与北向夹角和距离，求另一点 */
	public static destinationVincenty(point: Point, northAngle: number, distance: number): Point {
		/* 椭圆球基础参数*/
		let ct = {
			a: this.a,
			b: 6356752.3142,
			f: 1 / 298.257223563
		};
		let a = ct.a, b = ct.b, f = ct.f;

		let lon1 = point.lng;
		let lat1 = point.lat;

		let s = distance;
		let alpha1 = this.degToRad(northAngle);
		let sinAlpha1 = Math.sin(alpha1);
		let cosAlpha1 = Math.cos(alpha1);

		let tanU1 = (1 - f) * Math.tan(this.degToRad(lat1));
		let cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1)), sinU1 = tanU1 * cosU1;
		let sigma1 = Math.atan2(tanU1, cosAlpha1);
		let sinAlpha = cosU1 * sinAlpha1;
		let cosSqAlpha = 1 - sinAlpha * sinAlpha;
		let uSq = cosSqAlpha * (a * a - b * b) / (b * b);
		let A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
		let B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));

		let sigma = s / (b * A), sigmaP = 2 * Math.PI;
		let cos2SigmaM = 0, sinSigma = 0, cosSigma = 0;
		while (Math.abs(sigma - sigmaP) > 1e-12) {
			cos2SigmaM = Math.cos(2 * sigma1 + sigma);
			sinSigma = Math.sin(sigma);
			cosSigma = Math.cos(sigma);
			let deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) -
				B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
			sigmaP = sigma;
			sigma = s / (b * A) + deltaSigma;
		}

		let tmp = sinU1 * sinSigma - cosU1 * cosSigma * cosAlpha1;
		let lat2 = Math.atan2(sinU1 * cosSigma + cosU1 * sinSigma * cosAlpha1,
			(1 - f) * Math.sqrt(sinAlpha * sinAlpha + tmp * tmp));
		let lambda = Math.atan2(sinSigma * sinAlpha1, cosU1 * cosSigma - sinU1 * sinSigma * cosAlpha1);
		let C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
		let L = lambda - (1 - C) * f * sinAlpha *
			(sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

		// let revAz = Math.atan2(sinAlpha, -tmp);
		return new Point(lon1 + this.radToDeg(L), this.radToDeg(lat2));
	}

	/** 根据一点的经纬度求另一点与其相对的北向夹角 */
	public static getNorthByPointAB(point_a: Point, point_b: Point): number {
		let point_c = new Point(point_a.lng, point_b.lat);
		let ab = this.getDistance(point_a, point_b);
		let bc = this.getDistance(point_b, point_c);
		// 0
		if (point_a.lng == point_b.lng && point_a.lat == point_b.lat) {
			return 0;
		}
		// 0 <= n <= 90
		if (point_a.lng <= point_b.lng && point_a.lat <= point_b.lat) {
			if (bc < ab) {
				let BAC = Math.asin(bc / ab);
				return this.radToDeg(BAC);
			} else {
				return 90;
			}
		}
		// 90 < n <= 180
		if (point_a.lng <= point_b.lng && point_a.lat > point_b.lat) {
			if (bc < ab) {
				let BAC = Math.asin(bc / ab);
				return 180 - this.radToDeg(BAC);
			} else {
				return 90;
			}
		}
		// 180 < n <= 270
		if (point_a.lng >= point_b.lng && point_a.lat >= point_b.lat) {
			if (bc < ab) {
				let BAC = Math.asin(bc / ab);
				return 180 + this.radToDeg(BAC);
			} else {
				return 270;
			}
		}
		// 270 < n <= 360
		if (point_a.lng >= point_b.lng && point_a.lat < point_b.lat) {
			if (bc < ab) {
				let BAC = Math.asin(bc / ab);
				return 360 - this.radToDeg(BAC);
			} else {
				return 270;
			}
		}
		return 0;
	}

}

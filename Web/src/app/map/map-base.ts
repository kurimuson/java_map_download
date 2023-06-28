import { CommonUtil } from '../util/common-util';
import { MapConfigOption } from './map-config-option';
import { MapSource } from './map-source';
import { MapWrap } from './draw/map-wrap';
import { GeoUtil } from "./geo-util";
import { Point } from "./entity/Point";
import { DEFAULT_LAYER_NAME } from "../common/common-var";

declare const elementResizeDetectorMaker: any;
declare const ol: any;

export const polygonFillColor = 'rgba(50, 138, 288, 0.3)';
export const polygonStrokeColor = 'rgba(0, 90, 200, 1)';

export class MapBase {

	protected mapObj: any;
	public getOlMap = () => this.mapObj;
	public getMapSource = (): MapSource => this.mapSource;

	// 地图设置
	private mapConfig: MapConfigOption = CommonUtil.getConfigCache();

	public updateMapConfig(config: MapConfigOption) {
		this.mapConfig = config;
	}

	// 当前XYZ图层
	private currentXyzName = '';
	private currentXyzLayers: Array<any> = [];
	// 图层名字
	public readonly drawLayerName: string = 'vector-draw';
	// 图层实例
	private gridLayer: any;
	private drawLayer: any;
	private drawSource: any;
	// 绘制交互
	private drawType = 'Polygon';
	private modifyInteraction: any;
	private drawInteraction: any;
	private snapInteraction: any;

	constructor(
		private dom: HTMLDivElement,
		private mapSource: MapSource,
		private callback: { onFinish?: Function, onResize?: Function },
	) {
		this.initMap(callback).then((e) => {
			callback.onFinish && callback.onFinish(e);
		});
	}

	/** 生成地图 */
	private initMap(callback: { onResize?: Function }): Promise<any> {
		let mapLayers = [];
		// 底图
		let tilesArray = this.mapSource.layers.get(this.mapConfig.layer);
		if (tilesArray == null) {
			tilesArray = this.mapSource.layers.get(DEFAULT_LAYER_NAME);
		}
		for (let i = 0; i < tilesArray.length; i++) {
			let tileLayer = new ol.layer.Tile({
				source: tilesArray[i].source,
				zIndex: 0,
			})
			tileLayer.setProperties({
				'name': tilesArray[i].name
			});
			mapLayers.push(tileLayer);
			this.currentXyzLayers.push(tileLayer);
		}
		this.currentXyzName = this.mapConfig.layer;
		// 网格
		let gridLayer = new ol.layer.Tile({
			source: new ol.source.TileDebug(),
			visible: this.mapConfig.grid,
			zIndex: 50
		});
		mapLayers.push(gridLayer);
		this.gridLayer = gridLayer;
		// 绘制层
		let vectorDrawSource = new ol.source.Vector();
		let vectorDrawLayer = new ol.layer.Vector({
			source: vectorDrawSource,
			zIndex: 21,
			style: new ol.style.Style({
				fill: new ol.style.Fill({
					color: polygonFillColor
				}),
				stroke: new ol.style.Stroke({
					color: polygonStrokeColor,
					width: 2
				}),
				image: new ol.style.Circle({
					radius: 7,
					fill: new ol.style.Fill({
						color: '#ffcc33'
					})
				})
			}),
		});
		vectorDrawLayer.setProperties({ 'name': this.drawLayerName });
		mapLayers.push(vectorDrawLayer);
		this.drawLayer = vectorDrawLayer;
		this.drawSource = vectorDrawSource;
		/** 实例化地图 */
		this.mapObj = new ol.Map({
			target: this.dom,
			layers: mapLayers,
			view: new ol.View({
				projection: 'EPSG:3857',
				center: ol.proj.fromLonLat([105.203317, 37.506176]),
				zoom: 4,
				maxZoom: 21,
				minZoom: 0
			}),
			interactions: [
				new ol.interaction.DragPan(),
				new ol.interaction.PinchZoom(),
				new ol.interaction.KeyboardPan(),
				new ol.interaction.KeyboardZoom(),
				new ol.interaction.MouseWheelZoom(),
				new ol.interaction.DragZoom(),
			],
			controls: [
				// new ol.control.Zoom(),
				new ol.control.ScaleLine(),
			]
		});
		// 点击事件优化
		this.mapObj.on('click', (e: any) => {
			if (this.mapObj == null) {
				return;
			}
			let cover = 0;
			this.mapObj.forEachFeatureAtPixel(e.pixel, (e: any) => {
				cover = cover + 1;
				// if (cover == 1 && !this.isDistanceRulerOpen) {
				// 只接受zIndex最上层的Feature的点击事件
				// 在测距工具时禁止其他全部的Feature的点击事件
				e.dispatchEvent('click');
				// }
			});
		});
		// 尺寸自适应
		elementResizeDetectorMaker().listenTo(this.dom, (e: HTMLElement) => {
			setTimeout(() => {
				this.mapObj?.setSize([e.offsetWidth, e.offsetHeight]);
				this.mapObj?.getView().setViewportSize([e.offsetWidth, e.offsetHeight]);
				callback.onResize && callback.onResize(e);
			});
		});
		// 异步返回
		return new Promise((resolve) => {
			setTimeout(() => {
				resolve({
					success: true,
					msg: `地图加载完成`
				});
			});
		});
	}

	/** 销毁地图 */
	public destroyMap(): void {
		elementResizeDetectorMaker().uninstall(this.dom);
	}

	/** zoomIn */
	zoomIn(): void {
		if (this.mapObj != null) {
			let z = this.mapObj.getView().getZoom();
			z != null && this.mapObj.getView().setZoom(z + 1);
		}
	}

	/** zoomOut */
	zoomOut(): void {
		if (this.mapObj != null) {
			let z = this.mapObj.getView().getZoom();
			z != null && this.mapObj.getView().setZoom(z - 1);
		}
	}

	/** 设置最大Zoom */
	setMaxZoom(z: number): void {
		if (this.mapObj != null) {
			this.mapObj.getView().setMaxZoom(z);
		}
	}

	/** 获取当前坐标类型 */
	getCurrentCoordinateType(): string {
		let arr = this.mapSource.layers.get(this.currentXyzName);
		if (arr == null) {
			arr = this.mapSource.layers.get(DEFAULT_LAYER_NAME);
		}
		return arr[0].coordinateType;
	}

	/** 获取当前图层URL */
	getCurrentXyzUrlResources(): Array<string> {
		let keyType = CommonUtil.needKey(this.getCurrentXyzName()).type;
		let canChangeKey = CommonUtil.needKey(this.getCurrentXyzName()).has;
		let urls: Array<string> = [];
		let arr = this.mapSource.layers.get(this.currentXyzName);
		for (let i = 0; i < arr.length; i++) {
			switch (arr[i].type) {
				case 'XYZ_URL':
					let url = arr[i].url;
					if (canChangeKey) {
						let conf = CommonUtil.getConfigCache();
						if (keyType == 'tian') {
							urls.push(url.replace('&&&&&&&key&&&&&&&', conf.key[keyType]));
						} else {
							urls.push(url);
						}
					} else {
						urls.push(url);
					}
					break;
				default:
					break;
			}
		}
		return urls;
	}

	/** 获取当前图层NAME */
	getCurrentXyzName(): string {
		return this.currentXyzName;
	}

	/** 根据name获取图层 */
	getLayerByName(name: string): any {
		if (this.mapObj == null) {
			return null;
		}
		let layers = this.mapObj.getLayers().getArray();
		for (let i = 0; i < layers.length; i++) {
			if (name == layers[i].getProperties().name) {
				return layers[i];
			}
		}
		return null;
	}

	/** 获取图层是否显示 */
	getLayerVisibleByName(name: string): boolean {
		let layer = this.getLayerByName(name);
		if (layer) {
			return layer.getVisible();
		} else {
			return false;
		}
	}

	/** 显示网格 */
	showGrid(): void {
		this.gridLayer && this.gridLayer.setVisible(true);
	}

	/** 关闭网格 */
	closeGrid(): void {
		this.gridLayer && this.gridLayer.setVisible(false);
	}

	/** 网格是否显示 */
	getGridVisible(): boolean {
		if (this.gridLayer == null) {
			return false;
		}
		return this.gridLayer.getVisible();
	}

	/** setFitview */
	setFitviewFromDrawLayer(): void {
		if (this.mapObj == null) {
			return;
		}
		let layer = this.getLayerByName(this.drawLayerName);
		if (layer == null) {
			return;
		}
		let source = layer.getSource();
		MapWrap.setFitViewByFeatures({
			map: this,
			features: source.getFeatures(),
			padding: [32, 32, 32, 32]
		})
	}

	/** 更换地图类型 */
	switchMapResource(name: string): void {
		if (this.mapObj == null) {
			return;
		}
		// 删除当前图层
		for (let i = 0; i < this.currentXyzLayers.length; i++) {
			this.mapObj.removeLayer(this.currentXyzLayers[i]);
		}
		this.currentXyzLayers.splice(0, this.currentXyzLayers.length);
		// 设置新图层
		let newArr = this.mapSource.layers.get(name);
		for (let i = 0; i < newArr.length; i++) {
			let addLayer = new ol.layer.Tile({
				source: newArr[i].source,
				zIndex: 0,
			});
			addLayer.setProperties({
				'name': newArr[i].name,
			});
			this.mapObj.addLayer(addLayer);
			this.currentXyzLayers.push(addLayer);
		}
		this.currentXyzName = name;
	}

	turnMapFeaturesFromWgs84ToGcj02() {
		if (this.drawSource == null) {
			return;
		}
		let features = this.drawSource.getFeatures();
		if (features != null && features.length > 0) {
			for (let i = 0; i < features.length; i++) {
				let feature = features[i];
				let shape = feature.getGeometry();
				if (shape == null) {
					continue;
				}
				switch (shape.constructor) {
					case ol.geom.Polygon: {
						let sp = shape;
						let polygonPoints = sp.getCoordinates()[0];
						let wgs84Lnglat = [];
						for (let j = 0; j < polygonPoints.length; j++) {
							wgs84Lnglat.push(GeoUtil.Mercator_To_LngLat(new Point(polygonPoints[j][0], polygonPoints[j][1])));
						}
						let gcj02Lnglat = [];
						for (let j = 0; j < wgs84Lnglat.length; j++) {
							gcj02Lnglat.push(GeoUtil.wgs84_To_gcj02(new Point(wgs84Lnglat[j].lng, wgs84Lnglat[j].lat)));
						}
						let pts = [];
						for (let j = 0; j < gcj02Lnglat.length; j++) {
							pts.push(ol.proj.fromLonLat([gcj02Lnglat[j].lng, gcj02Lnglat[j].lat]));
						}
						sp.setCoordinates([pts]);
						break;
					}
					case ol.geom.Circle: {
						let sp = shape;
						let center = sp.getCenter();
						let wgs84Lnglat = GeoUtil.Mercator_To_LngLat(new Point(center[0], center[1]));
						let gcj02Lnglat = GeoUtil.wgs84_To_gcj02(new Point(wgs84Lnglat.lng, wgs84Lnglat.lat));
						let pt = ol.proj.fromLonLat([gcj02Lnglat.lng, gcj02Lnglat.lat]);
						sp.setCenter(pt);
						break;
					}
					default:
						break;
				}
			}
		}
	}

	turnMapFeaturesFromGcj02ToWgs84() {
		if (this.drawSource == null) {
			return;
		}
		let features = this.drawSource.getFeatures();
		if (features != null && features.length > 0) {
			for (let i = 0; i < features.length; i++) {
				let feature = features[i];
				let shape = feature.getGeometry();
				if (shape == null) {
					continue;
				}
				switch (shape.constructor) {
					case ol.geom.Polygon: {
						let sp = shape;
						let polygonPoints = sp.getCoordinates()[0];
						let gcj02Lnglat = [];
						for (let j = 0; j < polygonPoints.length; j++) {
							gcj02Lnglat.push(GeoUtil.Mercator_To_LngLat(new Point(polygonPoints[j][0], polygonPoints[j][1])));
						}
						let wgs84Lnglat = [];
						for (let j = 0; j < gcj02Lnglat.length; j++) {
							wgs84Lnglat.push(GeoUtil.gcj02_To_wgs84(new Point(gcj02Lnglat[j].lng, gcj02Lnglat[j].lat)));
						}
						let pts = [];
						for (let j = 0; j < wgs84Lnglat.length; j++) {
							pts.push(ol.proj.fromLonLat([wgs84Lnglat[j].lng, wgs84Lnglat[j].lat]));
						}
						sp.setCoordinates([pts]);
						break;
					}
					case ol.geom.Circle: {
						let sp = shape;
						let center = sp.getCenter();
						let gcj02Lnglat = GeoUtil.Mercator_To_LngLat(new Point(center[0], center[1]));
						let wgs84Lnglat = GeoUtil.gcj02_To_wgs84(new Point(gcj02Lnglat.lng, gcj02Lnglat.lat));
						let pt = ol.proj.fromLonLat([wgs84Lnglat.lng, wgs84Lnglat.lat]);
						sp.setCenter(pt);
						break;
					}
					default:
						break;
				}
			}
		}
	}

	setDrawType(type: string): void {
		this.drawType = type;
	}

	openDraw(callback: { drawEnd: Function, modifyEnd: Function }): void {
		switch (this.drawType) {
			case 'Polygon':
				this.openPolygonDraw(callback);
				break;
			case 'Circle':
				this.openCircleDraw(callback);
				break;
			default:
				break;
		}
	}

	getDrawedPoints(): number[][][] {
		let points = [];
		let features = this.drawSource?.getFeatures();
		if (features != null && features.length > 0) {
			for (let i = 0; i < features.length; i++) {
				let feature = features[i];
				let shape = feature.getGeometry();
				if (shape == null) {
					continue;
				}
				switch (shape.constructor) {
					case ol.geom.Polygon: {
						let polygonPoints = shape.getCoordinates()[0];
						if (polygonPoints != null && polygonPoints.length > 3) {
							points.push(polygonPoints);
						}
						break;
					}
					case ol.geom.Circle: {
						let center = shape.getCenter();
						let radius = shape.getRadius();
						let circlePoints = GeoUtil.getCirclePoints(GeoUtil.Mercator_To_LngLat(new Point(center[0], center[1])), radius);
						let mecCirclePoints = [];
						for (let j = 0; j < circlePoints.length; j++) {
							let pt = GeoUtil.LngLat_To_Mercator(circlePoints[j]);
							mecCirclePoints.push([pt.lng, pt.lat]);
						}
						points.push(mecCirclePoints);
						break;
					}
					default:
						break;
				}
			}
		}
		return points;
	}

	getDrawedPointsString(): string {
		let points = this.getDrawedPoints();
		if (points.length == 0) {
			return '';
		} else {
			return JSON.stringify(points);
		}
	}

	/** 绘制多边形 */
	private openPolygonDraw(callback: { drawEnd: Function, modifyEnd: Function }) {
		if (this.mapObj == null || this.drawSource == null) {
			return;
		}
		this.removeModifyInteraction();
		this.removeDrawInteraction();
		this.removeSnapInteraction();
		let modify = new ol.interaction.Modify({
			source: this.drawSource,
		});
		let draw = new ol.interaction.Draw({
			source: this.drawSource,
			type: 'Polygon'
		});
		let snap = new ol.interaction.Snap({
			source: this.drawSource,
		});
		modify.on('modifyend', () => {
			callback.modifyEnd()
		});
		draw.on('drawend', () => {
			callback.drawEnd()
		});
		this.mapObj.addInteraction(modify);
		this.mapObj.addInteraction(draw);
		this.mapObj.addInteraction(snap);
		this.modifyInteraction = modify;
		this.drawInteraction = draw;
		this.snapInteraction = snap;
	}

	/** 绘制圆形 */
	private openCircleDraw(callback: { drawEnd: Function, modifyEnd: Function }) {
		if (this.mapObj == null) {
			return;
		}
		this.removeModifyInteraction();
		this.removeDrawInteraction();
		this.removeSnapInteraction();
		let modify = new ol.interaction.Modify({
			source: this.drawSource,
		});
		let draw = new ol.interaction.Draw({
			source: this.drawSource,
			type: 'Circle'
		});
		let snap = new ol.interaction.Snap({
			source: this.drawSource,
		});
		modify.on('modifyend', () => {
			callback.modifyEnd()
		});
		draw.on('drawend', () => {
			callback.drawEnd()
		});
		this.mapObj.addInteraction(modify);
		this.mapObj.addInteraction(draw);
		this.mapObj.addInteraction(snap);
		this.modifyInteraction = modify;
		this.drawInteraction = draw;
		this.snapInteraction = snap;
	}

	/** 获取绘制层的图形 */
	getDrawedFeatures() {
		if (this.drawSource == null) {
			return;
		}
		return this.drawSource.getFeatures();
	}

	/** 删除绘制层的图形 */
	removeDrawedFeatures(): void {
		MapWrap.removeAllFeatures(this, this.drawLayerName);
	}

	/** 移除绘制交互 */
	removeModifyInteraction(): void {
		if (this.mapObj == null) {
			return;
		}
		if (this.modifyInteraction != null) {
			this.mapObj.removeInteraction(this.modifyInteraction);
			this.modifyInteraction = undefined;
		}
	}

	removeDrawInteraction(): void {
		if (this.mapObj == null) {
			return;
		}
		if (this.drawInteraction != null) {
			this.mapObj.removeInteraction(this.drawInteraction);
			this.drawInteraction = undefined;
		}
	}

	removeSnapInteraction(): void {
		if (this.mapObj == null) {
			return;
		}
		if (this.snapInteraction != null) {
			this.mapObj.removeInteraction(this.snapInteraction);
			this.snapInteraction = undefined;
		}
	}

	/** Pan */
	pan(): void {
		this.removeModifyInteraction();
		this.removeDrawInteraction();
		this.removeSnapInteraction();
	}

}

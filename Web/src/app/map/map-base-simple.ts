declare const elementResizeDetectorMaker: any;
declare const ol: any;

export class MapBaseSimple {

	protected mapObj: any;
	public getOlMap = () => this.mapObj;

	private gridLayer: any;

	constructor(
		private dom: HTMLDivElement,
		private config: { tileUrl: string },
		private callback?: { onFinish?: Function, onResize?: Function },
	) {
		this.initMap(config, callback).then((e) => {
			callback?.onFinish && callback?.onFinish(e);
		});
	}

	/** 生成地图 */
	private initMap(
		config: { tileUrl: string },
		callback?: { onResize?: Function },
	): Promise<any> {
		let mapLayers = [];
		// XYZ
		let tileLayer = new ol.layer.Tile({
			source: new ol.source.XYZ({
				url: config.tileUrl,
			}),
			zIndex: 0,
		});
		mapLayers.push(tileLayer);
		// 网格
		let gridLayer = new ol.layer.Tile({
			source: new ol.source.TileDebug(),
			visible: true,
			zIndex: 50
		});
		mapLayers.push(gridLayer);
		this.gridLayer = gridLayer;
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
				new ol.control.Zoom(),
				new ol.control.ScaleLine(),
			]
		});
		// 尺寸自适应
		elementResizeDetectorMaker().listenTo(this.dom, (e: HTMLElement) => {
			setTimeout(() => {
				this.mapObj?.setSize([e.offsetWidth, e.offsetHeight]);
				this.mapObj?.getView().setViewportSize([e.offsetWidth, e.offsetHeight]);
				callback?.onResize && callback?.onResize(e);
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

	/** 显示网格 */
	showGrid(): void {
		this.gridLayer && this.gridLayer.setVisible(true);
	}

	/** 关闭网格 */
	closeGrid(): void {
		this.gridLayer && this.gridLayer.setVisible(false);
	}

}

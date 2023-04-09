import { Topic } from "../../rx/inner-mq/topic";
import { CommonUtil } from '../../util/common-util';
import { MapPage } from '../../view/page/map/map.page';
import { MapDraw } from '../../map/draw/map-draw';
import { MapWrap } from '../../map/draw/map-wrap';
import { GeoUtil } from "../../map/geo-util";
import { Point } from "../../map/entity/Point";
import { MapSource } from "../../map/map-source";
import { DEFAULT_LAYER_NAME } from "../../common/common-var";

export class MapMessageProcessor {

	constructor(
		private mapPage: MapPage,
	) {
		mapPage.getMqClient().sub<string>(Topic.INIT_MAP_CONFIG, (res) => {
			// 来自服务端的配置文件
			let resData = <{
				addedLayers: Array<{ name: string, url: string, type: string }>,
			}>JSON.parse(res);
			// 添加图层
			let mapSource = new MapSource();
			if (resData.addedLayers != null) {
				mapSource.putAddedLayers(resData.addedLayers);
			}
			// 生成地图
			mapPage.initMap({ mapSource: mapSource });
		});
		/** 放大 */
		mapPage.getMqClient().sub(Topic.ZOOM_IN, (res) => {
			mapPage.getMapBase().zoomIn();
		});
		/** 缩小 */
		mapPage.getMqClient().sub(Topic.ZOOM_OUT, (res) => {
			mapPage.getMapBase().zoomOut();
		});
		/** 拖动 */
		mapPage.getMqClient().sub(Topic.PAN, (res) => {
			mapPage.getMapBase().pan();
		});
		/** fit view - */
		mapPage.getMqClient().sub(Topic.FIT_VIEW, (res) => {
			mapPage.getMapBase().setFitviewFromDrawLayer();
		});
		/** 显示网格 */
		mapPage.getMqClient().sub(Topic.GRID_SWITCH, (res) => {
			let update;
			if (mapPage.getMapBase().getGridVisible()) {
				mapPage.getMapBase().closeGrid();
				update = false;
			} else {
				mapPage.getMapBase().showGrid();
				update = true;
			}
			let config = CommonUtil.getConfigCache();
			config.grid = update;
			CommonUtil.saveConfigCache(config);
			mapPage.getMapBase().updateMapConfig(config);
		});
		/** 切换图层源 */
		mapPage.getMqClient().sub<string>(Topic.SWITCH_RESOURCE, (res) => {
			// 切换图层
			let lastType = mapPage.getMapBase().getCurrentCoordinateType();
			mapPage.getMapBase().switchMapResource(res);
			let currentType = mapPage.getMapBase().getCurrentCoordinateType();
			// 保存设置
			let config = CommonUtil.getConfigCache();
			config.layer = res;
			CommonUtil.saveConfigCache(config);
			mapPage.getMapBase().updateMapConfig(config);
			// 检查坐标类型
			if (lastType != currentType) {
				if (lastType == 'wgs84' && currentType == 'gcj02') {
					mapPage.getMapBase().turnMapFeaturesFromWgs84ToGcj02();
				} else if (lastType == 'gcj02' && currentType == 'wgs84') {
					mapPage.getMapBase().turnMapFeaturesFromGcj02ToWgs84();
				}
			}
			// 回调
			setTimeout(() => {
				mapPage.updateShowInfo();
			});
		});
		/** 切换自定义图层源 */
		mapPage.getMqClient().sub<string>(Topic.SWITCH_ADDED_RESOURCE, (res) => {
			let resData = <{ name: string, url: string, type: string }>JSON.parse(res);
			// 保存自定义图层
			mapPage.getMapBase().getMapSource().putAddedLayers([resData]);
			// 本地回环发送，调用上述方法
			mapPage.getMqClient().pub(Topic.SWITCH_RESOURCE, resData.name);
		});
		/** 删除自定义图层源 */
		mapPage.getMqClient().sub<string>(Topic.REMOVE_ADDED_RESOURCE, (res) => {
			// 本地回环发送，切换回默认地图
			mapPage.getMqClient().pub(Topic.SWITCH_RESOURCE, DEFAULT_LAYER_NAME);
			// 删除自定义图层
			mapPage.getMapBase().getMapSource().removeAddedLayers(res);
		});
		/** 绘制类型切换 - */
		mapPage.getMqClient().sub<string>(Topic.SWITCH_DRAW_TYPE, (res) => {
			mapPage.getMapBase().setDrawType(res);
		});
		/** 绘制 - */
		mapPage.getMqClient().sub(Topic.OPEN_DRAW, (res) => {
			mapPage.getMapBase().pan();
			mapPage.getMapBase().removeDrawedFeatures();
			mapPage.getMapBase().openDraw({
				drawEnd: () => {
					setTimeout(() => {
						mapPage.getMapBase().removeDrawInteraction();
					})
				},
				modifyEnd: () => {
				}
			});
		});
		/** 绘制指定多边形并定位 - */
		mapPage.getMqClient().sub<string>(Topic.DRAW_POLYGON_AND_POSITING, (res) => {
			mapPage.getMapBase().pan();
			mapPage.getMapBase().removeDrawedFeatures();
			let blocks = JSON.parse(res);
			for (let i = 0; i < blocks.length; i++) {
				let points: Array<Point> = [];
				for (let j = 0; j < blocks[i].length; j++) {
					let point = new Point(blocks[i][j].lng, blocks[i][j].lat);
					if (mapPage.getMapBase().getCurrentCoordinateType() == 'wgs84') {
						points.push(GeoUtil.gcj02_To_wgs84(point));
					} else {
						points.push(point);
					}
				}
				let feature = MapDraw.createPolygonFeature(points);
				MapWrap.addFeature(mapPage.getMapBase(), mapPage.getMapBase().drawLayerName, feature);
			}
			mapPage.getMapBase().setFitviewFromDrawLayer();
		});
		/** 删除绘制 - */
		mapPage.getMqClient().sub(Topic.REMOVE_SHAPE, (res) => {
			mapPage.getMapBase().removeDrawedFeatures();
		});
		/** 提交区块下载 - */
		mapPage.getMqClient().sub(Topic.SUBMIT_BLOCK_DOWNLOAD, (res) => {
			let data = {
				tileName: this.mapPage.getMapBase()?.getCurrentXyzName(),
				mapType: CommonUtil.getMapType(this.mapPage.getMapBase()?.getCurrentXyzName()),
				tileUrl: this.mapPage.getMapBase()?.getCurrentXyzUrlResources(),
				points: this.mapPage.getMapBase()?.getDrawedPoints(),
			};
			mapPage.getSubmitService().blockDownload(data).then((r) => {
			});
		});
		/** 提交世界下载 - */
		mapPage.getMqClient().sub(Topic.SUBMIT_WORLD_DOWNLOAD, (res) => {
			let data = {
				tileName: this.mapPage.getMapBase()?.getCurrentXyzName(),
				mapType: CommonUtil.getMapType(this.mapPage.getMapBase()?.getCurrentXyzName()),
				tileUrl: this.mapPage.getMapBase()?.getCurrentXyzUrlResources()
			};
			mapPage.getSubmitService().worldDownload(data).then((r) => {
			});
		});
	}

}

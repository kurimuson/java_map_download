import { CommonUtil } from "src/app/util/common-util";

declare const ol: any;

export class TianSource {

	TiandituNormalNone_Name: string;
	TiandituNormalNone_Source: Array<any>;

	TiandituTerrainNone_Name: string;
	TiandituTerrainNone_Source: Array<any>;

	TiandituLine_Name: string;
	TiandituLine_Source: Array<any>;

	TiandituTip_Name: string;
	TiandituTip_Source: Array<any>;

	constructor() {
		// 天地图-普通图-无标注
		const TiandituNormalNone_URL = "https://t{0-7}.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL={x}&TILEROW={y}&TILEMATRIX={z}&tk=&&&&&&&key&&&&&&&";
		this.TiandituNormalNone_Name = 'Tianditu-Normal-None';
		this.TiandituNormalNone_Source = [
			{
				name: "Normal-None", type: "XYZ_URL", support: true,
				source: new ol.source.XYZ({ url: TiandituNormalNone_URL.replace("&&&&&&&key&&&&&&&", CommonUtil.getConfigCache().key.tian) }),
				url: TiandituNormalNone_URL,
				coordinateType: "wgs84"
			}
		];
		// 天地图-地形图-无标注
		const TiandituTerrainNone_URL = "https://t{0-7}.tianditu.gov.cn/ter_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL={x}&TILEROW={y}&TILEMATRIX={z}&tk=&&&&&&&key&&&&&&&";
		this.TiandituTerrainNone_Name = 'Tianditu-Terrain-None';
		this.TiandituTerrainNone_Source = [
			{
				name: "Terrain-None", type: "XYZ_URL", support: true,
				source: new ol.source.XYZ({ url: TiandituTerrainNone_URL.replace("&&&&&&&key&&&&&&&", CommonUtil.getConfigCache().key.tian) }),
				url: TiandituTerrainNone_URL,
				coordinateType: "wgs84"
			}
		];
		// 天地图-边界线
		const TiandituLine_URL = "https://t{0-7}.tianditu.gov.cn/ibo_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ibo&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL={x}&TILEROW={y}&TILEMATRIX={z}&tk=&&&&&&&key&&&&&&&";
		this.TiandituLine_Name = 'Tianditu-Line';
		this.TiandituLine_Source = [
			{
				name: "Normal-Tip", type: "XYZ_URL", support: true,
				source: new ol.source.XYZ({ url: TiandituLine_URL.replace("&&&&&&&key&&&&&&&", CommonUtil.getConfigCache().key.tian) }),
				url: TiandituLine_URL,
				coordinateType: "wgs84"
			}
		];
		// 天地图-标注层
		const TiandituTip_URL = "https://t{0-7}.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL={x}&TILEROW={y}&TILEMATRIX={z}&tk=&&&&&&&key&&&&&&&";
		this.TiandituTip_Name = 'Tianditu-Tip';
		this.TiandituTip_Source = [
			{
				name: "Normal-Tip", type: "XYZ_URL", support: true,
				source: new ol.source.XYZ({ url: TiandituTip_URL.replace("&&&&&&&key&&&&&&&", CommonUtil.getConfigCache().key.tian) }),
				url: TiandituTip_URL,
				coordinateType: "wgs84"
			}
		];
	}

}


declare const ol: any;

export class AmapSource {

	AMapNormal_Name: string;
	AMapNormal_Source: Array<any>;

	AMapNormalNone_Name: string;
	AMapNormalNone_Source: Array<any>;

	AMapSatelliteNone_Name: string;
	AMapSatelliteNone_Source: Array<any>;

	AMapStreet_Name: string;
	AMapStreet_Source: Array<any>;

	AMapStreetNone_Name: string;
	AMapStreetNone_Source: Array<any>;


	constructor() {
		// 高德地图-普通图-带标注
		const AMapNormal_URL = 'https://webrd0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8';
		this.AMapNormal_Name = 'AMap-Normal';
		this.AMapNormal_Source = [
			{
				name: 'Normal', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: AMapNormal_URL }),
				url: AMapNormal_URL,
				coordinateType: 'gcj02',
			},
		];
		// 高德地图-普通图-无标注
		const AMapNormalNone_URL = 'https://webrd0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8&ltype=11';
		this.AMapNormalNone_Name = 'AMap-Normal-None';
		this.AMapNormalNone_Source = [
			{
				name: 'Normal-None', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: AMapNormalNone_URL }),
				url: AMapNormalNone_URL,
				coordinateType: 'gcj02',
			},
		];
		// 高德地图-影像图-无标注
		const AMapSatelliteNone_URL = 'https://webst0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=6';
		this.AMapSatelliteNone_Name = 'AMap-Satellite-None';
		this.AMapSatelliteNone_Source = [
			{
				name: 'Satellite-None', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: AMapSatelliteNone_URL }),
				url: AMapSatelliteNone_URL,
				coordinateType: 'gcj02',
			},
		];
		// 高德地图-路网图-带标注
		const AMapStreet_URL = 'https://webst0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8';
		this.AMapStreet_Name = 'AMap-Street';
		this.AMapStreet_Source = [
			{
				name: 'Street', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: AMapStreet_URL }),
				url: AMapStreet_URL,
				coordinateType: 'gcj02',
			},
		];
		// 高德地图-路网图-无标注
		const AMapStreetNone_URL = 'https://webst0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8&ltype=11';
		this.AMapStreetNone_Name = 'AMap-Street-None';
		this.AMapStreetNone_Source = [
			{
				name: 'Street-None', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: AMapStreetNone_URL }),
				url: AMapStreetNone_URL,
				coordinateType: 'gcj02',
			},
		];
	}

}






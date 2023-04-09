declare const ol: any;

export class GoogleSource {

	GoogleNormal_Name: string;
	GoogleNormal_Source: Array<any>;

	GoogleTerrain_Name: string;
	GoogleTerrain_Source: Array<any>;

	GoogleSatellite_Name: string;
	GoogleSatellite_Source: Array<any>;

	GoogleSatelliteNone_Name: string;
	GoogleSatelliteNone_Source: Array<any>;

	GoogleStreet_Name: string;
	GoogleStreet_Source: Array<any>;

	constructor() {
		// 谷歌地图-普通图-带标注
		const GoogleNormal_URL = 'https://mt{1-3}.google.com/maps/vt?lyrs=m%40781&hl=zh-CN&gl=CN&x={x}&y={y}&z={z}';
		this.GoogleNormal_Name = 'Google-Normal';
		this.GoogleNormal_Source = [
			{
				name: 'Normal', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: GoogleNormal_URL }),
				url: GoogleNormal_URL,
				coordinateType: 'gcj02',
			},
		];
		// 谷歌地图-地形图-带标注
		const GoogleTerrain_URL = 'https://mt{1-3}.google.com/maps/vt?lyrs=p%40781&hl=zh-CN&gl=CN&x={x}&y={y}&z={z}';
		this.GoogleTerrain_Name = 'Google-Terrain';
		this.GoogleTerrain_Source = [
			{
				name: 'Terrain', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: GoogleTerrain_URL }),
				url: GoogleTerrain_URL,
				coordinateType: 'gcj02',
			},
		];
		// 谷歌地图-影像图-带标注
		const GoogleSatellite_URL = 'https://mt{1-3}.google.com/maps/vt?lyrs=y%40781&hl=zh-CN&gl=CN&x={x}&y={y}&z={z}';
		this.GoogleSatellite_Name = 'Google-Satellite';
		this.GoogleSatellite_Source = [
			{
				name: 'Satellite', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: GoogleSatellite_URL }),
				url: GoogleSatellite_URL,
				coordinateType: 'gcj02',
			}
		];
		// 谷歌地图-影像图-无标注
		const GoogleSatelliteNone_URL = 'https://mt{1-3}.google.com/maps/vt?lyrs=s%40781&hl=zh-CN&gl=CN&x={x}&y={y}&z={z}';
		this.GoogleSatelliteNone_Name = 'Google-Satellite-None';
		this.GoogleSatelliteNone_Source = [
			{
				name: 'Satellite-None', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: GoogleSatelliteNone_URL }),
				url: GoogleSatelliteNone_URL,
				coordinateType: 'gcj02',
			},
		];
		// 谷歌地图-路网图-带标注
		const GoogleStreet_URL = 'https://mt{1-3}.google.com/maps/vt?lyrs=h%40781&hl=zh-CN&gl=CN&x={x}&y={y}&z={z}';
		this.GoogleStreet_Name = 'Google-Street';
		this.GoogleStreet_Source = [
			{
				name: 'Street', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: GoogleStreet_URL }),
				url: GoogleStreet_URL,
				coordinateType: 'gcj02',
			},
		];
	}

}

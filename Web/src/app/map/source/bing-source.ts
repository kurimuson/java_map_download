declare const ol: any;

export class BingSource {

	BingMapNormal1_Name: string;
	BingMapNormal1_Source: Array<any>;

	BingMapNormal1CN_Name: string;
	BingMapNormal1CN_Source: Array<any>;

	BingMapNormal1None_Name: string;
	BingMapNormal1None_Source: Array<any>;

	BingMapNormal2_Name: string;
	BingMapNormal2_Source: Array<any>;

	BingMapNormal2CN_Name: string;
	BingMapNormal2CN_Source: Array<any>;

	BingMapNormal2None_Name: string;
	BingMapNormal2None_Source: Array<any>;

	BingMapSatelliteNone_Name: string;
	BingMapSatelliteNone_Source: Array<any>;

	constructor() {
		const bingUrlTrans = (tileCoord: number[], url: string): string => {
			let result = '';
			let z = tileCoord[0];
			let x = tileCoord[1] + 1;
			let y = tileCoord[2] + 1;
			let z_all = Math.pow(2, z);
			for (let i = 1; i <= z; i++) {
				let z0 = z_all / Math.pow(2, i - 1);
				// 左上
				if (x / z0 <= 0.5 && y / z0 <= 0.5) {
					result = result + '0';
				}
				// 右上
				if (x / z0 > 0.5 && y / z0 <= 0.5) {
					result = result + '1';
					x = x - z0 / 2;
				}
				// 左下
				if (x / z0 <= 0.5 && y / z0 > 0.5) {
					result = result + '2';
					y = y - z0 / 2;
				}
				// 右下
				if (x / z0 > 0.5 && y / z0 > 0.5) {
					result = result + '3';
					x = x - z0 / 2;
					y = y - z0 / 2;
				}
			}
			return url.replace('{0-1}', Math.random() > 0.5 ? '0' : '1').replace('{&&&&&}', result);
		}
		// 'http://r{0-1}.tiles.ditu.live.com/tiles/r{&&&&&}.png?g=100&mkt=zh-cn';
		// 必应地图-普通图1-带标注-全球
		const BingMapNormal1_URL = 'https://t{0-1}.dynamic.tiles.ditu.live.com/comp/ch/{&&&&&}?mkt=zh-cn&n=z&it=G,TW,L&og=503&cstl=rd';
		this.BingMapNormal1_Name = 'Bing-Normal-1';
		this.BingMapNormal1_Source = [
			{
				name: 'Normal', type: 'XYZ_URL', support: true,
				source: new ol.source.TileImage({
					projection: 'EPSG:3857',
					tileUrlFunction: (tileCoord: any, pixelRatio: any, proj: any) => {
						return bingUrlTrans(tileCoord, BingMapNormal1_URL);
					}
				}),
				url: BingMapNormal1_URL,
				coordinateType: 'gcj02',
			},
		];
		// 必应地图-普通图1-带标注-国内
		const BingMapNormal1CN_URL = 'https://t{0-1}.dynamic.tiles.ditu.live.com/comp/ch/{&&&&&}?mkt=zh-cn&n=z&ur=CN&it=G,TW,L&og=503&cstl=rd';
		this.BingMapNormal1CN_Name = 'Bing-Normal-1-CN';
		this.BingMapNormal1CN_Source = [
			{
				name: 'Normal', type: 'XYZ_URL', support: true,
				source: new ol.source.TileImage({
					projection: 'EPSG:3857',
					tileUrlFunction: (tileCoord: any, pixelRatio: any, proj: any) => {
						return bingUrlTrans(tileCoord, BingMapNormal1CN_URL);
					}
				}),
				url: BingMapNormal1CN_URL,
				coordinateType: 'gcj02',
			},
		];
		// 必应地图-普通图1-无标注
		const BingMapNormal1None_URL = 'https://t{0-1}.dynamic.tiles.ditu.live.com/comp/ch/{&&&&&}?mkt=zh-cn&n=z&it=G,TW,RL&og=503&cstl=rd';
		this.BingMapNormal1None_Name = 'Bing-Normal-1-None';
		this.BingMapNormal1None_Source = [
			{
				name: 'Normal-None', type: 'XYZ_URL', support: true,
				source: new ol.source.TileImage({
					projection: 'EPSG:3857',
					tileUrlFunction: (tileCoord: any, pixelRatio: any, proj: any) => {
						return bingUrlTrans(tileCoord, BingMapNormal1None_URL);
					}
				}),
				url: BingMapNormal1None_URL,
				coordinateType: 'gcj02',
			},
		];
		// 必应地图-普通图2-带标注-全球
		let BingMapNormal2_URL = 'https://t{0-1}.dynamic.tiles.ditu.live.com/comp/ch/{&&&&&}?mkt=zh-cn&n=z&it=G,TW,L&og=503&cstl=vb';
		this.BingMapNormal2_Name = 'Bing-Normal-2';
		this.BingMapNormal2_Source = [
			{
				name: 'Normal', type: 'XYZ_URL', support: true,
				source: new ol.source.TileImage({
					projection: 'EPSG:3857',
					tileUrlFunction: (tileCoord: any, pixelRatio: any, proj: any) => {
						return bingUrlTrans(tileCoord, BingMapNormal2_URL);
					}
				}),
				url: BingMapNormal2_URL,
				coordinateType: 'gcj02',
			},
		];
		// 必应地图-普通图2-带标注-国内
		let BingMapNormal2CN_URL = 'https://t{0-1}.dynamic.tiles.ditu.live.com/comp/ch/{&&&&&}?mkt=zh-cn&n=z&ur=CN&it=G,TW,L&og=503&cstl=vb';
		this.BingMapNormal2CN_Name = 'Bing-Normal-2-CN';
		this.BingMapNormal2CN_Source = [
			{
				name: 'Normal', type: 'XYZ_URL', support: true,
				source: new ol.source.TileImage({
					projection: 'EPSG:3857',
					tileUrlFunction: (tileCoord: any, pixelRatio: any, proj: any) => {
						return bingUrlTrans(tileCoord, BingMapNormal2CN_URL);
					}
				}),
				url: BingMapNormal2CN_URL,
				coordinateType: 'gcj02',
			},
		];
		// 必应地图-普通图2-无标注
		let BingMapNormal2None_URL = 'https://t{0-1}.dynamic.tiles.ditu.live.com/comp/ch/{&&&&&}?mkt=zh-cn&n=z&it=G,TW,RL&og=503&cstl=vb';
		this.BingMapNormal2None_Name = 'Bing-Normal-2-None';
		this.BingMapNormal2None_Source = [
			{
				name: 'Normal-None', type: 'XYZ_URL', support: true,
				source: new ol.source.TileImage({
					projection: 'EPSG:3857',
					tileUrlFunction: (tileCoord: any, pixelRatio: any, proj: any) => {
						return bingUrlTrans(tileCoord, BingMapNormal2None_URL);
					}
				}),
				url: BingMapNormal2None_URL,
				coordinateType: 'gcj02',
			},
		];
		// 必应地图-影像图-无标注
		let BingMapSatelliteNone_URL = 'https://t{0-1}.dynamic.tiles.ditu.live.com/comp/ch/{&&&&&}?mkt=zh-cn&n=z&it=A&src=o&og=503';
		this.BingMapSatelliteNone_Name = 'Bing-Satellite-None';
		this.BingMapSatelliteNone_Source = [
			{
				name: 'Satellite-None', type: 'XYZ_URL', support: true,
				source: new ol.source.TileImage({
					projection: 'EPSG:3857',
					tileUrlFunction: (tileCoord: any, pixelRatio: any, proj: any) => {
						return bingUrlTrans(tileCoord, BingMapSatelliteNone_URL);
					}
				}),
				url: BingMapSatelliteNone_URL,
				coordinateType: 'wgs84'
			},
		];
	}

}

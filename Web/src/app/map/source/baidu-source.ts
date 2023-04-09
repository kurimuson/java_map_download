declare const ol: any;

export class BaiduSource {

	BaiduNormal_Name: string;
	BaiduNormal_Source: Array<any>;

	constructor() {
		const bdResolutions = [];
		for (let i = 0; i < 19; i++) {
			bdResolutions[i] = Math.pow(2, 18 - i);
		}
		const bdTilegrid = new ol.tilegrid.TileGrid({
			origin: [0, 0],
			resolutions: bdResolutions
		});
		// 百度地图-普通图-带标注
		this.BaiduNormal_Name = 'Baidu-Normal';
		this.BaiduNormal_Source = [
			{
				name: 'Normal', type: 'TILE_FUNC', support: false,
				source: new ol.source.TileImage({
					projection: 'EPSG:3857',
					tileGrid: bdTilegrid,
					tileUrlFunction: (tileCoord: any, pixelRatio: any, proj: any) => {
						if (!tileCoord) {
							return '';
						}
						let z = String(tileCoord[0]);
						let x = String(tileCoord[1]);
						let y = String(-tileCoord[2] - 1); //y坐标变成相反数
						if (Number(x) < 0) {
							x = 'M' + (-Number(x));
						}
						if (Number(y) < 0) {
							y = 'M' + (-Number(y));
						}
						return 'http://online1.map.bdimg.com/onlinelabel/?qt=tile&x=' + x + '&y=' + y + '&z=' + z + '&styles=pl&scaler=1&p=1'
					}
				}),
			},
		];
	}

}


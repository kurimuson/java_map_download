declare const ol: any;

export class TencentSource {

	TencentNormal_Name: string;
	TencentNormal_Source: Array<any>;

	constructor() {
		// 腾讯地图-普通图-带标注
		const TencentNormal_URL = 'http://rt0.map.gtimg.com/realtimerender?z={z}&x={x}&y={-y}&type=vector&style=0';
		this.TencentNormal_Name = 'Tencent-Normal';
		this.TencentNormal_Source = [
			{
				name: 'Normal', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: TencentNormal_URL }),
				url: TencentNormal_URL,
				coordinateType: 'gcj02',
			},
		];
	}

}


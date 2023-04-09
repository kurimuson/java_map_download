declare const ol: any;

export class OsmSource {

	OpenStreetMap_Name: string;
	OpenStreetMap_Source: Array<any>;

	constructor() {
		// OpenStreetMap
		const OpenStreetMap_URL = 'https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png';
		this.OpenStreetMap_Name = 'OpenStreet';
		this.OpenStreetMap_Source = [
			{
				name: 'Normal', type: 'XYZ_URL', support: true,
				source: new ol.source.XYZ({ url: OpenStreetMap_URL }),
				url: OpenStreetMap_URL,
				coordinateType: 'wgs84',
			}
		]
	}

}


declare const ol: any;

export class MapBoxSource {

	MapBoxPDF_Name: string;
	MapBoxPDF_Source: Array<any>;

	constructor() {
		// Calculation of resolutions that match zoom levels 1, 3, 5, 7, 9, 11, 13, 15.
		const resolutions = [];
		for (let i = 0; i <= 8; ++i) {
			resolutions.push(156543.03392804097 / Math.pow(2, i * 2));
		}

		// Calculation of tile urls for zoom levels 1, 3, 5, 7, 9, 11, 13, 15.
		const tileUrlFunction = (tileCoord: number[]) => {
			return MapBoxPDF_URL
				.replace('{z}', String(tileCoord[0] * 2 - 1))
				.replace('{x}', String(tileCoord[1]))
				.replace('{y}', String(tileCoord[2]))
				.replace(
					'{a-d}',
					'abcd'.substr(((tileCoord[1] << tileCoord[0]) + tileCoord[2]) % 4, 1)
				);
		}

		const key = 'pk.eyJ1IjoiaDYzMjU4MjE4MyIsImEiOiJjbDl6dHg4NmMwajI1M29uejc0bWV2ZHdvIn0.Zk2n4LahjuKP3TG8ih6iCg'
		// MapBox-PDF矢量瓦片
		const MapBoxPDF_URL = 'https://{a-d}.tiles.mapbox.com/v4/mapbox.mapbox-streets-v6/' +
			'{z}/{x}/{y}.vector.pbf?access_token=' + key;
		this.MapBoxPDF_Name = 'MapBox-PDF';
		this.MapBoxPDF_Source = [
			{
				name: 'MapBox', type: 'PDF', support: true,
				source: new ol.source.VectorTile({
					attributions:
						'© <a href="https://www.mapbox.com/map-feedback/">Mapbox</a> ' +
						'© <a href="https://www.openstreetmap.org/copyright">' +
						'OpenStreetMap contributors</a>',
					format: new ol.format.MVT(),
					tileGrid: new ol.tilegrid.TileGrid({
						extent: ol.proj.get('EPSG:3857').getExtent(),
						resolutions: resolutions,
						tileSize: 512,
					}),
					tileUrlFunction: tileUrlFunction,
				}),
				url: MapBoxPDF_URL,
				coordinateType: 'gcj02',
			},
		];
	}

}

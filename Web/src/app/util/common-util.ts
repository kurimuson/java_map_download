import * as lodash from "lodash-es";
import { MapConfigOption } from '../map/map-config-option';
import { DEFAULT_LAYER_NAME, TIAN_MAP_KEY } from "../common/common-var";

export class CommonUtil {

	public static getMapType(name: string): string {
		switch (name) {
			case 'OpenStreet':
				return 'OpenStreet';
			case 'Google-Normal':
				return '谷歌地图（普通带标注）';
			case 'Google-Terrain':
				return '谷歌地图（地形带标注）';
			case 'Google-Satellite':
				return '谷歌地图（影像带标注）';
			case 'Google-Satellite-None':
				return '谷歌地图（影像无标注）';
			case 'Google-Street':
				return '谷歌地图（路网带标注）';
			case 'AMap-Normal':
				return '高德地图（普通带标注）';
			case 'AMap-Normal-None':
				return '高德地图（普通无标注）';
			case 'AMap-Satellite-None':
				return '高德地图（影像无标注）';
			case 'AMap-Street':
				return '高德地图（路网带标注）';
			case 'AMap-Street-None':
				return '高德地图（路网无标注）';
			case 'Tencent-Normal':
				return '腾讯地图（普通带标注）';
			case 'Tianditu-Normal-None':
				return '天地图（普通无标注）';
			case 'Tianditu-Terrain-None':
				return '天地图（地形无标注）';
			case 'Tianditu-Line':
				return '天地图（边界无标注）';
			case 'Tianditu-Tip':
				return '天地图（标注层）';
			case 'Bing-Normal-1':
				return '必应地图（普通1带标注-全球）';
			case 'Bing-Normal-1-CN':
				return '必应地图（普通1带标注-国内）';
			case 'Bing-Normal-1-None':
				return '必应地图（普通1无标注）';
			case 'Bing-Normal-2':
				return '必应地图（普通2带标注-全球）';
			case 'Bing-Normal-2-CN':
				return '必应地图（普通2带标注-国内）';
			case 'Bing-Normal-2-None':
				return '必应地图（普通2无标注）';
			case 'Bing-Satellite':
				return '必应地图（影像带标注-全球）';
			case 'Bing-Satellite-CN':
				return '必应地图（影像带标注-国内）';
			case 'Bing-Satellite-None':
				return '必应地图（影像无标注）';
			default:
				return '自定义图层：' + name;
		}
	}

	public static needKey(name: string | undefined): { type: string, has: boolean } {
		if (name?.indexOf('Tianditu') == 0) {
			return { type: 'tian', has: true };
		} else {
			return { type: '', has: false };
		}
	}

	public static saveConfigCache(config: MapConfigOption): void {
		localStorage.setItem('jmd-config', JSON.stringify(config));
	}

	public static getConfigCache(): MapConfigOption {
		let json, config;
		json = localStorage.getItem('jmd-config');
		if (json && json != '') {
			config = JSON.parse(json);
		} else {
			config = {
				layer: DEFAULT_LAYER_NAME,
				grid: true,
				key: {
					tian: TIAN_MAP_KEY,
				},
			}
		}
		return config;
	}

	public static removeConfigCache(): void {
		localStorage.removeItem('jmd-config');
	}

	/** 生成随机数 */
	public static randomNum(minNum: number, maxNum: number): number {
		switch (arguments.length) {
			case 1:
				return parseInt((Math.random() * minNum + 1).toString(), 10);
			case 2:
				return parseInt((Math.random() * (maxNum - minNum + 1) + minNum).toString(), 10);
			default:
				return 0;
		}
	}

	/** 四舍五入 */
	public static round(num: number, e: number): number {
		return lodash.round(num, e);
	}

	/** 深拷贝 */
	public static cloneDeep<T>(data: any): T {
		return lodash.cloneDeep(data);
	}

}

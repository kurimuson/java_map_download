import { Injectable } from '@angular/core';
import { HttpClientService } from '../http/http-client.service';
import { HttpMethod } from '../http/http-method';
import { RESTfulResult } from '../http/restful-result.type';

@Injectable()
export class InfoService {

	constructor(private http: HttpClientService) {
	}

	/** 获取WS路径 */
	public getWsPath(): Promise<RESTfulResult> {
		return this.http.request({
			method: HttpMethod.GET,
			url: '/info/getWsPath',
			data: null,
		})
	}

}

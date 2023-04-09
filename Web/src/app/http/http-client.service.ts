import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpParamsEntity } from './http-params.entity';
import { HttpMethod } from './http-method';
import { lastValueFrom } from 'rxjs';

@Injectable()
export class HttpClientService {

	constructor(
		private http: HttpClient,
	) {
	}

	public request(params: HttpParamsEntity): any {
		switch (params.method) {
			case HttpMethod.GET:
				return this.get(params.url, params.data === null ? {} : params.data);
			case HttpMethod.POST:
				return this.post(params.url, params.data === null ? {} : params.data);
			default:
				break;
		}
	}

	private get(url: string, params: any): Promise<any> {
		return lastValueFrom(this.http.get(url, {
			params: params,
			responseType: 'json'
		}));
	}

	private post(url: string, params: any): Promise<any> {
		return lastValueFrom(this.http.post(url, params, {
			responseType: 'json'
		}));
	}

}

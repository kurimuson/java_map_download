import { HttpMethod } from 'src/app/http/http-method';

export class HttpParamsEntity {

    method: HttpMethod;
    url: string;
    data: any;

    constructor(method: HttpMethod, url: string, data: any) {
        this.method = method;
        this.url = url;
        this.data = data;
    }

}

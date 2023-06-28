import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {

	private readonly production: boolean = environment.production;

	constructor() {
	}

	intercept(req: HttpRequest<any>, next: HttpHandler) {
		let url;
		if (this.production) {
			url = `http://localhost:${window.location.port}${req.url}`;
		} else {
			url = `http://localhost:26737${req.url}`;
		}
		// 设置请求头
		let newReq = req.clone({
			url: url,
		});
		// send cloned request with header to the next handler.
		return next.handle(newReq).pipe(
			retry(3),
			catchError(this.handleError)
		);
	}

	private handleError(error: HttpErrorResponse) {
		console.error(`Backend returned code ${error.status},body was: `, error.error);
		// return an observable with a user-facing error message
		return throwError('Something bad happened; please try again later.');
	}

}

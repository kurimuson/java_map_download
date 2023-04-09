import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NgZorroAntdModule } from './ng-zorro-antd.module';
import { registerLocaleData } from '@angular/common';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientService } from './http/http-client.service';
import { HttpInterceptorProvider } from './http/http-interceptor.provider';
import { InnerMqService } from "./rx/inner-mq/service/inner-mq.service";

import { NZ_I18N } from 'ng-zorro-antd/i18n';
import { zh_CN } from 'ng-zorro-antd/i18n';
import zh from '@angular/common/locales/zh';

registerLocaleData(zh);

@NgModule({
	declarations: [
		AppComponent
	],
	imports: [
		BrowserModule,
		BrowserAnimationsModule,
		FormsModule,
		HttpClientModule,
		AppRoutingModule,
		NgZorroAntdModule,
	],
	providers: [
		HttpClientService,
		HttpInterceptorProvider,
		InnerMqService,
		{ provide: NZ_I18N, useValue: zh_CN },
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientService } from './http/http-client.service';
import { HttpInterceptorProvider } from './http/http-interceptor.provider';
import { InnerMqService } from "./rx/inner-mq/service/inner-mq.service";


@NgModule({
	declarations: [
		AppComponent
	],
	imports: [
		BrowserModule,
		BrowserAnimationsModule,
		HttpClientModule,
		AppRoutingModule,
	],
	providers: [
		HttpClientService,
		HttpInterceptorProvider,
		InnerMqService,
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}

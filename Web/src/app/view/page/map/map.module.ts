import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { provideRouter } from "@angular/router";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgZorroAntdModule } from 'src/app/ng-zorro-antd.module';
import { MapPage } from './map.page';

@NgModule({
	imports: [
		CommonModule,
		FormsModule,
		ReactiveFormsModule,
		NgZorroAntdModule,
	],
	declarations: [
		MapPage,
	],
	providers: [
		provideRouter([
			{ path: '', component: MapPage },
		]),
	],
})
export class MapModule {
}

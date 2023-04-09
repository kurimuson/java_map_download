import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { provideRouter, RouterOutlet } from "@angular/router";
import { NgZorroAntdModule } from 'src/app/ng-zorro-antd.module';
import { IndexPage } from './index.page';

@NgModule({
	imports: [
		CommonModule,
		NgZorroAntdModule,
		RouterOutlet,
	],
	declarations: [
		IndexPage,
	],
	providers: [
		provideRouter([
			{ path: '', redirectTo: 'map', pathMatch: 'full' },
			{
				path: '', component: IndexPage, children: [
					{ // 地图页面
						path: 'map',
						loadChildren: () => import('./map/map.module').then(mod => mod.MapModule)
					},
				]
			},
		]),
	],
})
export class IndexModule {
}

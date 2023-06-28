import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { provideRouter, RouterOutlet } from "@angular/router";
import { IndexPage } from './index.page';

@NgModule({
	imports: [
		CommonModule,
		RouterOutlet,
	],
	declarations: [
		IndexPage,
	],
	providers: [
		provideRouter([
			{ path: '', redirectTo: 'map-control', pathMatch: 'full' },
			{
				path: '', component: IndexPage, children: [
					{ // 地图操作页面
						path: 'map-control',
						loadChildren: () => import('./map-control/map-control.page-module').then(mod => mod.MapControlPageModule)
					},
					{ // 瓦片预览页面
						path: 'tile-view',
						loadChildren: () => import('./tile-view/tile-view.page-module').then(mod => mod.TileViewPageModule)
					},
				]
			},
		]),
	],
})
export class IndexPageModule {
}

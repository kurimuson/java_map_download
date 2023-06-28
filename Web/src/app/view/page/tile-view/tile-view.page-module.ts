import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { provideRouter } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";
import { TileViewPage } from "./tile-view.page";

@NgModule({
	imports: [
		CommonModule,
		MatButtonModule,
	],
	declarations: [
		TileViewPage,
	],
	providers: [
		provideRouter([
			{ path: '', component: TileViewPage },
		]),
	],
})
export class TileViewPageModule {
}

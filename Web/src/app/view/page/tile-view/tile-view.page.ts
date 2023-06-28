import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { MapBaseSimple } from "src/app/map/map-base-simple";

@Component({
	selector: 'app-tile-view',
	templateUrl: './tile-view.page.html',
	styleUrls: ['./tile-view.page.scss'],
})
export class TileViewPage implements OnInit, OnDestroy {

	@ViewChild('map', { static: true }) mapEleRef!: ElementRef<HTMLDivElement>;

	zoom: number = 0;
	gridOpen: boolean = true;

	private mapBase!: MapBaseSimple;

	ngOnInit(): void {
		this.mapBase = new MapBaseSimple(this.mapEleRef.nativeElement, {
			tileUrl: 'http://localhost:26737/tile/local?z={z}&x={x}&y={y}',
		}, {
			onFinish: () => {
				this.mapBase.getOlMap().on('moveend', (e: any) => {
					let z = e.map.getView().getZoom();
					if (z != null) {
						this.zoom = Math.round(z);
					}
				})
			},
		});
	}

	ngOnDestroy(): void {
		this.mapBase.destroyMap();
	}

	gridOpenClose(): void {
		this.gridOpen = !this.gridOpen;
		this.gridOpen ? this.mapBase.showGrid() : this.mapBase.closeGrid();
	}

}

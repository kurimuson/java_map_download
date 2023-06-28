import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MapMessageConnection } from "src/app/connection/onmassage/map-message.connection";
import { InnerMqService } from "src/app/rx/inner-mq/service/inner-mq.service";
import { InnerMqClient } from "src/app/rx/inner-mq/client/inner-mq.client";
import { MapBase } from 'src/app/map/map-base';
import { InfoService } from "src/app/service/info.service";
import { SubmitService } from 'src/app/service/submit.service';
import { CommonUtil } from 'src/app/util/common-util';
import { MapMessageProcessor } from 'src/app/connection/onmassage/map-message.processor';
import { MapSource } from "src/app/map/map-source";
import { MatDialog } from "@angular/material/dialog";
import { KeyInputDialogComponent } from "./dialog/key-input-dialog.component";

@Component({
	selector: 'app-map-control',
	templateUrl: './map-control.page.html',
	styleUrls: ['./map-control.page.scss'],
	providers: [InfoService, SubmitService],
})
export class MapControlPage implements OnInit, OnDestroy {

	@ViewChild('map', { static: true }) mapEleRef!: ElementRef<HTMLDivElement>;
	@ViewChild('mapTypeDiv', { static: true }) mapTypeDiv!: ElementRef<HTMLElement>;
	@ViewChild('changeKeyDiv', { static: true }) changeKeyDiv!: ElementRef<HTMLDivElement>;

	private websocket!: MapMessageConnection;

	mapType: string = '';
	coordinateType: string = '';
	zoom: number = 0;
	keyType: string = '';

	private client!: InnerMqClient;
	private mapBase!: MapBase;

	public getMqClient = (): InnerMqClient => this.client;
	public getMapBase = (): MapBase => this.mapBase;
	public getSubmitService = (): SubmitService => this.submitService;

	constructor(
		private dialog: MatDialog,
		private innerMqService: InnerMqService,
		private infoService: InfoService,
		private submitService: SubmitService,
	) {
	}

	ngOnInit(): void {
		this.client = this.innerMqService.createLoClient();
		this.infoService.getWsPath().then((res) => {
			this.websocket = new MapMessageConnection(res.data, this.innerMqService);
			new MapMessageProcessor(this);
		})
	}

	ngOnDestroy(): void {
		this.websocket.disConnection();
		this.innerMqService.destroyClient(this.client);
		this.mapBase.destroyMap();
	}

	initMap(config: { mapSource: MapSource }): void {
		this.mapBase = new MapBase(this.mapEleRef.nativeElement, config.mapSource, {
			onFinish: () => {
				this.mapBase.getOlMap().on('moveend', (e: any) => {
					let z = e.map.getView().getZoom();
					if (z != null) {
						this.zoom = Math.round(z);
					}
				})
				this.updateShowInfo();
			},
		});
	}

	updateShowInfo(): void {
		this.keyType = CommonUtil.needKey(this.mapBase.getCurrentXyzName()).type;
		let canChangeKey = CommonUtil.needKey(this.mapBase.getCurrentXyzName()).has;
		if (canChangeKey) {
			this.changeKeyDiv.nativeElement.style.display = 'block';
		} else {
			this.changeKeyDiv.nativeElement.style.display = 'none';
		}
		this.mapType = CommonUtil.getMapType(this.mapBase.getCurrentXyzName());
		this.coordinateType = this.mapBase.getCurrentCoordinateType();
	}

	openChangeKeyModal(): void {
		if (this.keyType == 'tian') {
			this.dialog.open(KeyInputDialogComponent, {
				width: '500px',
				data: {
					defaultKey: CommonUtil.getConfigCache().key[this.keyType],
					callback: (key: string) => {
						this.changeKey(key);
					}
				}
			})
		}
	}

	changeKey(key: string): void {
		if (this.keyType == 'tian') {
			let config = CommonUtil.getConfigCache();
			config.key[this.keyType] = key;
			CommonUtil.saveConfigCache(config);
			this.reload();
		}
	}

	reload(): void {
		window.location.reload();
	}

}

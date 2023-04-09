import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InnerMqService } from "src/app/rx/inner-mq/service/inner-mq.service";
import { InnerMqClient } from "src/app/rx/inner-mq/client/inner-mq.client";
import { MapBase } from 'src/app/map/map-base';
import { SubmitService } from 'src/app/service/submit.service';
import { CommonUtil } from 'src/app/util/common-util';
import { MapMessageProcessor } from '../../../connection/onmassage/map-message.processor';
import { MapSource } from "../../../map/map-source";

@Component({
	selector: 'app-map',
	templateUrl: './map.page.html',
	styleUrls: ['./map.page.less'],
	providers: [SubmitService],
})
export class MapPage implements OnInit, OnDestroy {

	@ViewChild('map', { static: true }) mapEleRef!: ElementRef<HTMLDivElement>;
	@ViewChild('mapTypeDiv', { static: true }) mapTypeDiv!: ElementRef<HTMLElement>;
	@ViewChild('changeKeyDiv', { static: true }) changeKeyDiv!: ElementRef<HTMLDivElement>;

	mapType: string = '';
	coordinateType: string = '';
	zoom: number = 0;
	keyType: string = '';
	isChangeKeyModalVisible: boolean = false;

	private client!: InnerMqClient;
	private mapBase!: MapBase;

	public getMqClient = (): InnerMqClient => this.client;
	public getMapBase = (): MapBase => this.mapBase;
	public getSubmitService = (): SubmitService => this.submitService;

	validateForm!: FormGroup;

	constructor(
		private fb: FormBuilder,
		private innerMqService: InnerMqService,
		private submitService: SubmitService,
	) {
	}

	ngOnInit(): void {
		this.client = this.innerMqService.createLoClient();
		this.validateForm = this.fb.group({
			key: [null, [Validators.required]],
		});
		new MapMessageProcessor(this);
	}

	ngOnDestroy(): void {
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
			this.validateForm.controls['key'].setValue(CommonUtil.getConfigCache().key[this.keyType])
			this.isChangeKeyModalVisible = true;
		}
	}

	closeChangeKeyModal(): void {
		this.isChangeKeyModalVisible = false;
	}

	keyFormValid(): boolean {
		for (const i in this.validateForm.controls) {
			this.validateForm.controls[i].markAsDirty();
			this.validateForm.controls[i].updateValueAndValidity();
		}
		return this.validateForm.valid;
	}

	changeKey(): void {
		if (this.keyType == 'tian') {
			if (this.keyFormValid()) {
				let key = this.validateForm.controls['key'].value;
				let config = CommonUtil.getConfigCache();
				config.key[this.keyType] = key;
				CommonUtil.saveConfigCache(config);
				this.reload();
			}
		}
	}

	reload(): void {
		window.location.reload();
	}

}

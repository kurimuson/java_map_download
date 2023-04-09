import { Component, OnDestroy, OnInit } from '@angular/core';
import { InnerMqService } from "src/app/rx/inner-mq/service/inner-mq.service";
import { MapMessageConnection } from 'src/app/connection/onmassage/map-message.connection';
import { InfoService } from '../../service/info.service';

@Component({
	selector: 'app-index',
	templateUrl: './index.page.html',
	styleUrls: ['./index.page.less'],
	providers: [InfoService],
})
export class IndexPage implements OnInit, OnDestroy {

	private websocket!: MapMessageConnection;

	constructor(
		private innerMqService: InnerMqService,
		private infoService: InfoService,
	) {
	}

	ngOnInit(): void {
		this.infoService.getWsPath().then((res) => {
			this.websocket = new MapMessageConnection(res.data, this.innerMqService);
		})
	}

	ngOnDestroy(): void {
		this.websocket.disConnection();
	}

}

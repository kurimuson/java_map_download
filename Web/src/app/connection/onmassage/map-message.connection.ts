import { WebSocketConfigOption } from '../websocket/WebSocketConfigOption';
import { ReconnectableWebSocket } from '../websocket/ReconnectableWebSocket';
import { InnerMqService } from "../../rx/inner-mq/service/inner-mq.service";

export class MapMessageConnection {

	private ws!: ReconnectableWebSocket;

	constructor(
		private path: string,
		private innerMqService: InnerMqService,
	) {
		this.connection();
	}

	/** 连接 */
	private connection(): void {
		let wsConfig: WebSocketConfigOption = {
			url: this.path,
			onopen: () => {
			},
			onerror: () => {
			},
			onmessage: (msg: any) => {
				if (msg.data && msg.data !== '') {
					let data = JSON.parse(msg.data);
					this.innerMqService.pub(data.title, data.content);
				}
			}
		}
		this.ws = new ReconnectableWebSocket(wsConfig);
	}

	/** 断开连接 */
	public disConnection(): void {
		this.ws.close();
	}

}

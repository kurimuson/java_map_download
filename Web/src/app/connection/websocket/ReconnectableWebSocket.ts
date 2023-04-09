import { WebSocketConfigOption } from './WebSocketConfigOption';

export class ReconnectableWebSocket {

	private ws!: WebSocket; // ws实例
	private opt: WebSocketConfigOption; // ws配置项
	private lockReconnect: boolean = false; // 避免ws重复连接
	private isClosingWindow: boolean = false;
	private reconnectTimeout: any;
	private heartSendInterval: any;

	constructor(option: WebSocketConfigOption) {
		if (null === option.url || '' === option.url) {
			throw ('url不能为空');
		}
		this.opt = option;
		this.initWebSocket();
	}

	private initWebSocket() {
		if (null == this.opt.secWebSocketProtocol) {
			this.ws = new WebSocket(this.opt.url);
		} else if (this.opt.secWebSocketProtocol.length == 0) {
			this.ws = new WebSocket(this.opt.url);
		} else {
			this.ws = new WebSocket(this.opt.url, this.opt.secWebSocketProtocol);
		}
		this.initEventHandle();
		window.onbeforeunload = () => {
			this.isClosingWindow = true;
			this.ws.close(); // 当窗口关闭时，主动去关闭websocket连接。
		}
	}

	private initEventHandle() {
		this.ws.onclose = () => {
			console.log('ws连接关闭!' + this.opt.url);
			this.opt.onclose && this.opt.onclose();
			this.heartCheckStop();
			if (!this.isClosingWindow) {
				this.reconnect();
			}
		}
		this.ws.onerror = () => {
			console.log('ws连接错误!' + this.opt.url);
			this.opt.onerror && this.opt.onerror();
			this.heartCheckStop();
			if (!this.isClosingWindow) {
				this.reconnect();
			}
		}
		this.ws.onopen = () => {
			console.log('ws连接成功!' + this.opt.url);
			this.opt.onopen && this.opt.onopen();
			this.heartCheckStart();
		}
		this.ws.onmessage = (event: any) => {
			this.opt.onmessage && this.opt.onmessage(event);
		}
	}

	/** 重连 */
	private reconnect() {
		if (this.lockReconnect) {
			return;
		}
		this.lockReconnect = true;
		this.reconnectTimeout = setTimeout(() => {
			this.initWebSocket();
			this.lockReconnect = false;
		}, 2000);
	}

	/** 关闭重连 */
	private reconnectStop(): void {
		clearTimeout(this.reconnectTimeout);
	}

	/** 开启心跳包保持连接 */
	private heartCheckStart(): void {
		this.ws.send('heartCheck');
		this.heartSendInterval = setInterval(() => {
			this.ws.send('heartCheck');
		}, 5 * 60 * 1000);
	}

	/** 关闭心跳包 */
	private heartCheckStop(): void {
		clearInterval(this.heartSendInterval);
	}

	/** 主动关闭连接 */
	public close(): void {
		this.reconnectStop();
		this.heartCheckStop();
		this.isClosingWindow = true;
		this.ws.close();
	}

}

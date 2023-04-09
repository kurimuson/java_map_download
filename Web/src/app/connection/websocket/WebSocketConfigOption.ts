export type WebSocketConfigOption = {

	url: string;
	secWebSocketProtocol?: Array<string>;
	onopen?: () => void;
	onmessage?: (msg: any) => void;
	onerror?: () => void;
	onclose?: () => void;

}

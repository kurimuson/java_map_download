package com.jmd.web.websocket.handler.base;

import com.jmd.web.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BaseWebSocketHandler extends TextWebSocketHandler {

	/** hashMap存取websocketSession，键值为HttpSession sessionID */
	public static Map<String, WebSocketSession> webSocketSessionMap = new HashMap<>();

	/** onOpen */
	@Override
	public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
		String sessionIndex = (String) webSocketSession.getAttributes().get(Constants.WEBSOKET_SESSION_INDEX);
		webSocketSessionMap.put(sessionIndex, webSocketSession);
		log.info("Session Index: {} 建立websocket连接成功，当前数量:{}", sessionIndex, webSocketSessionMap.size());
	}

	/** onClose */
	@Override
	public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
		String sessionIndex = (String) webSocketSession.getAttributes().get(Constants.WEBSOKET_SESSION_INDEX);
		webSocketSessionMap.remove(sessionIndex);
		log.info("Session Index: {} websocket连接关闭", sessionIndex);
	}

	/** onMessage */
	@Override
	protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws Exception {
		super.handleTextMessage(webSocketSession, message);
	}

	/** onError */
	@Override
	public void handleTransportError(WebSocketSession webSocketSession, Throwable exception) throws Exception {
		if (webSocketSession.isOpen()) {
			webSocketSession.close();
		}
		String sessionIndex = (String) webSocketSession.getAttributes().get(Constants.WEBSOKET_SESSION_INDEX);
		webSocketSessionMap.remove(sessionIndex);
		log.info("Session Index: {} websocket连接异常", sessionIndex);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/** websocket发消息给指定用户 */
	public void sendMessageToUser(String sessionKey, TextMessage message) {
		WebSocketSession webSocketSession = webSocketSessionMap.get(sessionKey);
		if (null != webSocketSession && webSocketSession.isOpen()) {
			try {
				webSocketSession.sendMessage(message);
			} catch (IOException e) {
				log.error("WebSocket SendMessage Exception", e);
			}
		}
	}

}

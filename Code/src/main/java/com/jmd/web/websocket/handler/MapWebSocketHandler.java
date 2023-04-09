package com.jmd.web.websocket.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jmd.ApplicationSetting;
import com.jmd.common.WsSendTopic;
import com.jmd.web.common.WsSendData;
import com.jmd.web.websocket.handler.base.BaseWebSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Component
public class MapWebSocketHandler extends BaseWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        super.afterConnectionEstablished(webSocketSession);
        var addedLayers = ApplicationSetting.getSetting().getAddedLayers();
        var mapConfig = new HashMap<>();
        mapConfig.put("addedLayers", addedLayers);
        var data = new WsSendData(WsSendTopic.INIT_MAP_CONFIG, JSON.toJSONString(mapConfig));
        this.send(data);
    }

    public void send(WsSendData data) {
        for (String key : webSocketSessionMap.keySet()) {
            sendMessageToUser(key, new TextMessage(JSON.toJSONString(data)));
        }
    }

}

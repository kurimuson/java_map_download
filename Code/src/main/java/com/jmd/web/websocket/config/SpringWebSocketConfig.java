package com.jmd.web.websocket.config;

import com.jmd.web.websocket.handler.MapWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class SpringWebSocketConfig extends WebMvcConfigurationSupport implements WebSocketConfigurer {

    @Autowired
    private MapWebSocketHandler mapWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(mapWebSocketHandler, "/websocket/map")
                .addInterceptors(new SpringWebSocketHandlerInterceptor()).setAllowedOrigins("*");
    }

}

package com.jmd.web.websocket.config;

import com.jmd.web.common.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class SpringWebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        HttpSession session = getSession(request);
        if (null != session && null == session.getAttribute("kickout")) {
            String[] uri = request.getURI().toString().split("/");
            String index = uri[uri.length - 1];
            String sessionIndex = Constants.WEBSOKET_SESSION_INDEX + "_" + session.getId() + "_" + index;
            attributes.put(Constants.WEBSOKET_SESSION_INDEX, sessionIndex);
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
        if (httpRequest.getHeader("sec-websocket-protocol") != null) {
            httpResponse.addHeader("sec-websocket-protocol", httpRequest.getHeader("sec-websocket-protocol"));
        }
        super.afterHandshake(request, response, wsHandler, exception);
    }

    @Nullable
    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest().getSession(true);
        }
        return null;
    }

}

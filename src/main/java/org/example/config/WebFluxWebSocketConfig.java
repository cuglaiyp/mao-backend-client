package org.example.config;

import org.example.util.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.socket.server.support.WebSocketHandlerMapping;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebFlux
public class WebFluxWebSocketConfig {


    @Autowired
    private MessageHandler messageHandler;  // 注入消息处理类

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler() {
            @Override
            public Mono<Void> handle(WebSocketSession session) {
                // 接收到消息时的处理逻辑
                return session.receive()
                        .map(webSocketMessage -> {
                            String message = webSocketMessage.getPayloadAsText();
                            // 将消息交给 MessageHandler 处理
                            return messageHandler.handleMessage(session, message);
                        })
                        .then();
            }
        };
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public WebSocketHandlerMapping webSocketHandlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/cat", webSocketHandler());  // 配置 WebSocket 端点
        WebSocketHandlerMapping handlerMapping = new WebSocketHandlerMapping();
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }
}

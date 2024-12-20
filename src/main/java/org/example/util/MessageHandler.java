package org.example.util;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class MessageHandler {

    public Mono<Void> handleMessage(WebSocketSession session, String message) {
        // 这里可以处理接收到的消息，做一些业务逻辑处理
        // 比如解析消息、验证消息的格式、进行数据库查询等
        System.out.println("Received message: " + message);

        // 根据消息内容做不同的响应
        if (message.equals("hello")) {
            return session.send(Mono.just(session.textMessage("Hello, client!")));
        } else {
            return session.send(Mono.just(session.textMessage("Unknown message")));
        }
    }
}

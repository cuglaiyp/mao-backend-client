package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

@Component
public class WebSocketSessionManager {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();  // 使用线程安全的集合

    @Autowired
    @Qualifier("asyncExecutor")  // 使用自定义线程池
    private Executor executor;

    // 添加连接
    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    // 移除连接
    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    // 广播消息到所有连接
    public Mono<Void> broadcastMessage(String message) {
        // 使用线程池异步处理广播消息
        return Mono.fromRunnable(() -> {
            Flux.fromIterable(sessions)
                    .flatMap(session -> session.send(Mono.just(session.textMessage(message))))
                    .subscribeOn(Schedulers.fromExecutor(executor))  // 使用自定义线程池来执行消息广播
                    .subscribe();
        });
    }
}


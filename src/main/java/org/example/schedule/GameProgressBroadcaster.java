package org.example.schedule;

import org.example.config.WebSocketSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class GameProgressBroadcaster {

    @Autowired
    private WebSocketSessionManager sessionManager;

    @Async("asyncExecutor")  // 指定使用自定义线程池
    @Scheduled(fixedRate = 1000, timeUnit = TimeUnit.MILLISECONDS)
    public void broadcastGameProgress() {
        String message = "Current game progress: " + Math.random() * 100;
        sessionManager.broadcastMessage(message)
                .subscribe();  // 广播消息
    }
}


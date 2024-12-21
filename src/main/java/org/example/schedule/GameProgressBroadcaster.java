package org.example.schedule;

import org.example.controller.GameController;
import org.example.handler.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class GameProgressBroadcaster {

    private static float progressFlag = 0;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Async(value = "broadcastExecutor")  // 指定使用自定义线程池
    @Scheduled(fixedRate = 100, timeUnit = TimeUnit.MILLISECONDS)
    public void broadcastGameProgress() {
        if(Float.compare(GameController.getProgress(), progressFlag) != 0) {
            webSocketHandler.broadcastGameMessage();
            progressFlag = GameController.getProgress();
        }
    }
}


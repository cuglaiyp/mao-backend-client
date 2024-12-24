package org.example.schedule;

import org.example.handler.WebSocketHandler;
import org.example.manager.InfoManager;
import org.example.pojo.GameInfo;
import org.example.pojo.SceneInfo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class GameProgressBroadcaster {

    private static float progressFlag = 0;
    private static float onlineFlag = 0;

    @Async(value = "broadcastExecutor")  // 指定使用自定义线程池
    @Scheduled(fixedRate = 200, timeUnit = TimeUnit.MILLISECONDS)
    public void broadcastGameInfo() {
        if (Float.compare(InfoManager.getProgress(), progressFlag) != 0 && InfoManager.sceneInfo.getStatus() == 1) {
            WebSocketHandler.broadcastGameMessage();
            progressFlag = InfoManager.getProgress();
        }
        if (InfoManager.player2Session.size() != onlineFlag) {
            WebSocketHandler.broadcastOnlineMessage();
            onlineFlag = InfoManager.player2Session.size();
        }
    }
}


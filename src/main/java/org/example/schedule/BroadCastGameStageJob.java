package org.example.schedule;

import org.example.controller.GameController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BroadCastGameStageJob {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static float broadCastProgress = 0;

    @Scheduled(fixedRate = 100, timeUnit = TimeUnit.MILLISECONDS)
    public void broadCastGameStage() {
        if (GameController.gameStage.getStatus() == 1 && Float.compare(broadCastProgress, GameController.gameStage.getProgress()) != 0) {
            messagingTemplate.convertAndSend("/topic/game", GameController.gameStage);
            broadCastProgress = GameController.gameStage.getProgress();
        }
    }
}

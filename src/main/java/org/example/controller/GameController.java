package org.example.controller;

import org.example.pojo.GameStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
public class GameController {

    private static GameStage gameStage = new GameStage();
    private static volatile int totalCnt = 0;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/boost")  // 映射客户端发送的消息
    @SendTo("/topic/game")    // 广播消息到所有订阅了 /topic/game 的客户端
    public GameStage onBoost(String player) {
        if (gameStage.getProgress() == 100) {
            gameStage.setRunning(false);
            messagingTemplate.convertAndSend("topic/ctrl", gameStage);
            return gameStage;
        }
        totalCnt += 1;
        ConcurrentHashMap<String, Integer> player2Score = gameStage.getPlayer2Score();
        Integer orDefault = player2Score.getOrDefault(player, 0);
        player2Score.put(player, orDefault + 1);
        gameStage.setProgress(getProgress());
        // 处理助力逻辑，更新进度
        return gameStage;
    }

    // 用于客户端新用户同步进度
    @GetMapping("init")
    public GameStage init() {
        gameStage.setProgress(getProgress());
        gameStage.setRunning(false);
        return gameStage;
    }

    // 用于管理端开启游戏
    @GetMapping("start")
    public GameStage start() {
        gameStage = new GameStage();
        gameStage.setRunning(true);
        messagingTemplate.convertAndSend("/topic/ctrl", gameStage);
        return gameStage;
    }

    private float getProgress() {
        if (totalCnt == 0 || gameStage.getPlayer2Score().isEmpty()) {
            return 0;
        }
        float progress = (float) totalCnt / gameStage.getPlayer2Score().size();
        return progress < 100 ? progress : 100;
    }
}

package org.example.controller;

import org.example.pojo.GameStage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GameController {

    private static GameStage gameStage = new GameStage();
    private static volatile int totalCnt = 0;

    @MessageMapping("/boost")  // 映射客户端发送的消息
    @SendTo("/topic/game")    // 广播消息到所有订阅了 /topic/game 的客户端
    public GameStage onBoost(String player) {
        totalCnt += 1;
        Map<String, Integer> player2Score = gameStage.getPlayer2Score();
        Integer orDefault = player2Score.getOrDefault(player, 0);
        player2Score.put(player, orDefault + 1);
        gameStage.setProgress(totalCnt / gameStage.getPlayer2Score().size());
        // 处理助力逻辑，更新进度
        return gameStage;
    }

    @GetMapping("process")
    public GameStage process() {
        gameStage.setProgress(totalCnt / gameStage.getPlayer2Score().size());
        // 处理助力逻辑，更新进度
        return gameStage;
    }
}

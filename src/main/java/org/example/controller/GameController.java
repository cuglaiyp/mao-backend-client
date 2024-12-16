package org.example.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.example.pojo.GameBoostMessage;
import org.example.pojo.GameProgress;

@Controller
public class GameController {

    @MessageMapping("/boost")  // 映射客户端发送的消息
    @SendTo("/topic/game")    // 广播消息到所有订阅了 /topic/game 的客户端
    public GameProgress onBoost(GameBoostMessage message) {
        // 处理助力逻辑，更新进度
        return new GameProgress(message.getPlayer(), message.getBoostCount());
    }

    public static void main(String[] args) {
        System.out.println("http://localhost:63342".equalsIgnoreCase("http://localhost:63342"));
    }
}

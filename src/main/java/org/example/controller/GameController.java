package org.example.controller;

import org.example.pojo.GameStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class GameController {

    private static GameStage gameStage = new GameStage();
    private static volatile int totalCnt = 0;
    private static ConcurrentHashMap<String, String> player2IP = new ConcurrentHashMap<>();
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/boost")  // 映射客户端发送的消息// 广播消息到所有订阅了 /topic/game 的客户端
    public GameStage onBoost(String player) {
        if (gameStage.getProgress() == 100) {
            gameStage.setStatus(2);
            messagingTemplate.convertAndSend("/topic/ctrl", gameStage);
            return gameStage;
        }
        totalCnt += 1;
        ConcurrentHashMap<String, Integer> player2Score = gameStage.getPlayer2Score();
        Integer orDefault = player2Score.getOrDefault(player, 0);
        player2Score.put(player, orDefault + 1);
        gameStage.setProgress(getProgress());
        messagingTemplate.convertAndSend("/topic/game", gameStage);
        // 处理助力逻辑，更新进度
        return gameStage;
    }

    // 用于客户端新用户同步进度
    @GetMapping("init")
    public GameStage init() {
        gameStage.setProgress(getProgress());
        return gameStage;
    }

    // 用于管理端开启游戏
    @GetMapping("start")
    public void start() {
        if (totalCnt == 0) {
            gameStage.setStatus(1);
            messagingTemplate.convertAndSend("/topic/ctrl", gameStage);
        }
    }

    @GetMapping("reset")
    public void reset() {
        totalCnt = 0;
        gameStage = new GameStage();
        gameStage.setStatus(0);
        messagingTemplate.convertAndSend("/topic/ctrl", gameStage);
    }


    private float getProgress() {
        if (totalCnt == 0 || gameStage.getPlayer2Score().isEmpty()) {
            return 0;
        }
        float progress = (float) totalCnt / gameStage.getPlayer2Score().size();
        return progress < 100 ? progress : 100;
    }

    @PostMapping("judgeName")
    public boolean judgeName(@RequestBody String playerName, HttpServletRequest request) {
        String clientIp = getClientIp(request);
        if (player2IP.containsKey(playerName) && !player2IP.get(playerName).equals(clientIp)) {
            return false;
        }
        player2IP.put(playerName, clientIp);
        ConcurrentHashMap<String, Integer> player2Score = gameStage.getPlayer2Score();
        if (!player2Score.containsKey(playerName)) {
            player2Score.put(playerName, 0);
        }
        return true;
    }

    public String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        } else {
            // 取第一个IP
            String[] ips = ipAddress.split(",");
            ipAddress = ips[0];
        }
        return ipAddress;
    }
}

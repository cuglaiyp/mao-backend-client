package org.example.controller;

import org.example.pojo.GameInfo;
import org.example.pojo.SceneInfo;
import org.pyj.yeauty.pojo.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;


public class GameController {

    public final static GameInfo gameInfo = new GameInfo();
    public final static SceneInfo sceneInfo = new SceneInfo();
    public final static ConcurrentHashMap<String, Session> player2Session = new ConcurrentHashMap<>();

    public void onBoost(String player) {
        //if (gameInfo.getProgress() == 100) {
        //    sceneInfo.setStatus(2);
        //    return;
        //}
        //sceneInfo.setTotalPointCnt(sceneInfo.getTotalPointCnt() + 1);
        ////ConcurrentHashMap<String, Integer> player2Score = gameInfo.getPlayer2Score();
        //Integer orDefault = player2Score.getOrDefault(player, 0);
        //player2Score.put(player, orDefault + 1);
        //gameInfo.setProgress(getProgress());
    }

    // 用于客户端新用户同步进度
      /*@GetMapping("init")
    public Map<String, Object> init() {
        Map<String, Object> res = new HashMap<>();
        gameInfo.setProgress(getProgress());
        res.put("gameInfo", gameInfo);
        res.put("sceneInfo", sceneInfo);
        return res;
    }

    // 用于管理端开启游戏
    @GetMapping("start")
    public void start() {
        if (sceneInfo.getTotalPointCnt() == 0) {
            sceneInfo.setStatus(1);
            messagingTemplate.convertAndSend("/topic/ctrl", sceneInfo);
        }
    }

    @GetMapping("reset")
    public void reset() {
        sceneInfo.reset();
        gameInfo.reset();
        messagingTemplate.convertAndSend("/topic/ctrl", sceneInfo);
    }*/

    public static float getProgress() {
        if (sceneInfo.getTotalPointCnt() == 0 || gameInfo.getPlayer2Score().isEmpty()) {
            return 0;
        }
        float progress = (float) sceneInfo.getTotalPointCnt() / gameInfo.getPlayer2Score().size();
        return progress < 100 ? progress : 100;
    }

    /*@PostMapping("judgeName")
    public boolean judgeName(@RequestBody String playerName, HttpServletRequest request) {
        if (playerName.length() > 4) {
            return false;
        }
        ConcurrentHashMap<String, String> player2IP = sceneInfo.getPlayer2IP();
        String clientIp = getClientIp(request);
        if (player2IP.containsKey(playerName) && !player2IP.get(playerName).equals(clientIp)) {
            return false;
        }
        player2IP.put(playerName, clientIp);
        return true;
    }

    @GetMapping("conn")
    public int conn() {
        System.out.println(connListener.getActiveConnections());
        return connListener.getActiveConnections();
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
    }*/
}

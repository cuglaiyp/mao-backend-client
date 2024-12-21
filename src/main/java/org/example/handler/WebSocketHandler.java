package org.example.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.pyj.yeauty.annotation.*;
import org.pyj.yeauty.pojo.Session;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.example.controller.GameController.*;

@Component
@ServerPath(path = "/mao")
public class WebSocketHandler {



    private static ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {{
        setCorePoolSize(20); // 核心线程数
        setMaxPoolSize(40);  // 最大线程数
        setQueueCapacity(200); // 队列容量
        setThreadNamePrefix("broader-"); // 线程名前缀
        initialize(); // 初始化线程池
    }};

    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
        session.setSubprotocols("stomp");
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String player, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
        session.setAttribute("player", player);
        player2Session.put(player, session);
        gameInfo.getPlayer2Score().putIfAbsent(player, 0);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        player2Session.remove(session.getAttribute("player"));
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        if (Float.compare(gameInfo.getProgress(), 100) == 0) {
            sceneInfo.setStatus(2);
            broadcastSceneMessage();
            return;
        }
        String player = message;
        sceneInfo.setTotalPointCnt(sceneInfo.getTotalPointCnt() + 1);
        ConcurrentHashMap<String, Integer> player2Score = gameInfo.getPlayer2Score();
        player2Score.put(player, player2Score.getOrDefault(player, 0) + 1);
        gameInfo.setProgress(getProgress());
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }

    public void broadcastGameMessage() {
        LinkedHashMap<String, Integer> top10Map = gameInfo.getPlayer2Score().entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue())) // 按分数降序排序
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Iterator<Map.Entry<String, Session>> iterator = player2Session.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Session> next = iterator.next();
            String player = next.getKey();
            Session session = next.getValue();
            if (!session.isActive()) {
                iterator.remove();
                continue;
            }
            Map msg = new HashMap();
            msg.put("type", 0);
            msg.put("player2Score", top10Map);
            msg.put("playerScore", gameInfo.getPlayer2Score().get(player));
            msg.put("progress", gameInfo.getProgress());
            executor.execute(() -> session.sendText(JSON.toJSONString(msg)));
        }
    }

    public void broadcastSceneMessage() {
        Iterator<Map.Entry<String, Session>> iterator = player2Session.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Session> next = iterator.next();
            String player = next.getKey();
            Session session = next.getValue();
            if (!session.isActive()) {
                iterator.remove();
                continue;
            }
            Map msg = new HashMap();
            msg.put("type", 1);
            msg.put("status", sceneInfo.getStatus());
            msg.put("onlineCnt", player2Session.size());
            executor.execute(() -> session.sendText(JSON.toJSONString(msg)));
        }
    }


}
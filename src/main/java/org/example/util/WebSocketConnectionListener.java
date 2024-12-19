package org.example.util;

import org.example.controller.GameController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketConnectionListener {

    private final AtomicInteger activeConnections = new AtomicInteger(0);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    // 获取当前连接数
    public int getActiveConnections() {
        return activeConnections.get();
    }

    // 增加连接数
    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        activeConnections.incrementAndGet();
        GameController.sceneInfo.setOnlineCnt(getActiveConnections());
        messagingTemplate.convertAndSend("/topic/ctrl", GameController.sceneInfo);
    }

    // 减少连接数
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        activeConnections.decrementAndGet();
        GameController.sceneInfo.setOnlineCnt(getActiveConnections());
        messagingTemplate.convertAndSend("/topic/ctrl", GameController.sceneInfo);
    }
}

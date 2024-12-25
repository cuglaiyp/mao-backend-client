package org.example;

import cn.hutool.core.util.RandomUtil;

import javax.websocket.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
public class WebSocketClient {

    private static List<Session> sessions = new ArrayList<>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    public static void main(String[] args) throws InterruptedException {
        try {
            for (int i = 0; i < 200; i++) {
                String serverURI = "ws://8.156.69.47:8080/mao?player=" + i; // 连接到 WebSocket 服务器
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                container.connectToServer(WebSocketClient.class, new URI(serverURI));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            Thread.sleep(10000);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("Connected to server");
        scheduler.scheduleAtFixedRate(() -> {
            sendMessage(session, RandomUtil.randomInt(0, 200) + "");
        }, 5, 1, TimeUnit.SECONDS);
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received from server: " + message);
    }

    @OnClose
    public void onClose() {
        System.out.println("Connection closed");
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    public void sendMessage(Session session, String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
                System.out.println("Sent message: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

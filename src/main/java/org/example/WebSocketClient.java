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
    private static List<String> userNames = new ArrayList<>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    public static void main(String[] args) throws InterruptedException {
        try {
            for (int i = 0; i < 1000; i++) {
                String userName = RandomUtil.randomString(4);
                userNames.add(userName);
                String serverURI = "ws://8.156.69.47:8080/mao?player=" + userName;  // 连接到 WebSocket 服务器
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                container.connectToServer(WebSocketClient.class, new URI(serverURI));
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        sessions.forEach(session -> {
            scheduler.scheduleAtFixedRate(() -> {
                WebSocketClient.sendMessage(session, userNames.get(RandomUtil.randomInt(userNames.size())));
            }, 5, 1, TimeUnit.SECONDS);
        });
        while (true) {
            Thread.sleep(10000);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received from server: " + message);
    }

    @OnClose
    public void onClose() {
        System.err.println("Connection closed");
    }

    @OnError
    public void onError(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }

    public static void sendMessage(Session session, String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
                System.out.println("Sent message: " + message);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

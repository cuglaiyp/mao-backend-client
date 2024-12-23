package org.example.controller;

import org.example.handler.WebSocketHandler;
import org.pyj.http.NettyHttpRequest;
import org.pyj.http.annotation.NettyHttpHandler;
import org.pyj.http.handler.IFunctionHandler;

import java.util.Map;

import static org.example.manager.InfoManager.sceneInfo;

@NettyHttpHandler(path = "/start", method = "GET")
public class GameStartController implements IFunctionHandler<Map<String, Object>> {

    @Override
    public Map<String, Object> execute(NettyHttpRequest request) {
        start();
        return null;
    }

    public void start() {
        if (sceneInfo.getTotalPointCnt() == 0) {
            sceneInfo.setStatus(1);
            WebSocketHandler.broadcastSceneMessage();
        }
    }
}
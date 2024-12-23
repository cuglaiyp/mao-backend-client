package org.example.controller;

import org.example.handler.WebSocketHandler;
import org.pyj.http.NettyHttpRequest;
import org.pyj.http.annotation.NettyHttpHandler;
import org.pyj.http.handler.IFunctionHandler;

import static org.example.manager.InfoManager.gameInfo;
import static org.example.manager.InfoManager.sceneInfo;

@NettyHttpHandler(path = "/reset", method = "GET")
public class GameResetController implements IFunctionHandler<Void> {

    @Override
    public Void execute(NettyHttpRequest request) {
        reset();
        return null;
    }

    public void reset() {
        sceneInfo.reset();
        gameInfo.reset();
        WebSocketHandler.broadcastSceneMessage();
    }
}
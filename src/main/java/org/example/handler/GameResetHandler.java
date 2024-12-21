package org.example.handler;

import org.pyj.http.NettyHttpRequest;
import org.pyj.http.annotation.NettyHttpHandler;
import org.pyj.http.handler.IFunctionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static org.example.controller.GameController.gameInfo;
import static org.example.controller.GameController.sceneInfo;

@Controller
@NettyHttpHandler(path = "/reset", method = "GET")
public class GameResetHandler implements IFunctionHandler<Void> {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Override
    public Void execute(NettyHttpRequest request) {
        reset();
        return null;
    }

    public void reset() {
        sceneInfo.reset();
        gameInfo.reset();
        webSocketHandler.broadcastSceneMessage();
    }
}
package org.example.handler;

import org.pyj.http.NettyHttpRequest;
import org.pyj.http.annotation.NettyHttpHandler;
import org.pyj.http.handler.IFunctionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static org.example.controller.GameController.sceneInfo;

@Controller
@NettyHttpHandler(path = "/start", method = "GET")
public class GameStartHandler implements IFunctionHandler<Map<String, Object>> {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Override
    public Map<String, Object> execute(NettyHttpRequest request) {
        start();
        return null;
    }

    // /judgename/啊
    // /judgeName

    public void start() {
        if (sceneInfo.getTotalPointCnt() == 0) {
            sceneInfo.setStatus(1);
            webSocketHandler.broadcastSceneMessage();
        }
    }
}
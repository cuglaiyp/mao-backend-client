package org.example.handler;


import org.example.controller.GameController;
import org.pyj.http.NettyHttpRequest;
import org.pyj.http.annotation.NettyHttpHandler;
import org.pyj.http.handler.IFunctionHandler;
import org.pyj.http.handler.Result;
import org.pyj.http.handler.ResultJson;

import java.util.HashMap;
import java.util.Map;

import static org.example.controller.GameController.*;

@NettyHttpHandler(path = "/init", method = "GET")
public class GameInitHandler implements IFunctionHandler<Map<String, Object>> {

    @Override
    public Map<String, Object> execute(NettyHttpRequest request) {
        return init();
    }

    public Map<String, Object> init() {
        Map<String, Object> res = new HashMap<>();
        gameInfo.setProgress(getProgress());
        sceneInfo.setOnlineCnt(player2Session.size());
        res.put("gameInfo", gameInfo);
        res.put("sceneInfo", sceneInfo);
        return res;
    }
}
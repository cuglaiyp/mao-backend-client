package org.example.controller;


import org.example.manager.InfoManager;
import org.pyj.http.NettyHttpRequest;
import org.pyj.http.annotation.NettyHttpHandler;
import org.pyj.http.handler.IFunctionHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@NettyHttpHandler(path = "/init", method = "GET", equal = false)
public class GameInitController implements IFunctionHandler<Map<String, Object>> {

    @Override
    public Map<String, Object> execute(NettyHttpRequest request) {
        String player = null;
        try {
            player = URLDecoder.decode(request.getStringPathValue(2), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return init(player);
    }

    public Map<String, Object> init(String player) {

        Map<String, Object> res = new HashMap<>();
        LinkedHashMap<String, Integer> top10Map = InfoManager.gameInfo.getPlayer2Score().entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue())) // 按分数降序排序
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Map gameInfo = new HashMap();
        gameInfo.put("player2Score", top10Map);
        gameInfo.put("playerScore", InfoManager.gameInfo.getPlayer2Score().get(player));
        gameInfo.put("progress", InfoManager.gameInfo.getProgress());
        Map sceneInfo = new HashMap();
        sceneInfo.put("status", InfoManager.sceneInfo.getStatus());
        sceneInfo.put("onlineCnt", InfoManager.player2Session.size());
        sceneInfo.put("xiCardWord", InfoManager.sceneInfo.getPlayer2Xi().get(player));
        res.put("gameInfo", gameInfo);
        res.put("sceneInfo", sceneInfo);
        return res;
    }
}
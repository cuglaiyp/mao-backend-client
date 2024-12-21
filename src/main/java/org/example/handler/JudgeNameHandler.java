package org.example.handler;

import org.pyj.http.NettyHttpRequest;
import org.pyj.http.annotation.NettyHttpHandler;
import org.pyj.http.handler.IFunctionHandler;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.controller.GameController.sceneInfo;

@NettyHttpHandler(path = "/judgeName", method = "GET", equal = false)
public class JudgeNameHandler implements IFunctionHandler<Map> {

    @Override
    public Map execute(NettyHttpRequest request) {
        String player = URLDecoder.decode(request.getStringPathValue(2));
        Map res = new HashMap();
        if (player.length() > 4) {
            res.put("msg", "名称过长，需小于5！");
            res.put("code", 1);
            return res;
        }
        ConcurrentHashMap<String, String> player2IP = sceneInfo.getPlayer2IP();
        String clientIp = request.getIp();
        if (player2IP.containsKey(player) && !player2IP.get(player).equals(clientIp)) {
            res.put("msg", "名称已存在！");
            res.put("code", 1);
            return res;
        }
        player2IP.put(player, clientIp);
        res.put("code", 0);
        return res;
    }


}
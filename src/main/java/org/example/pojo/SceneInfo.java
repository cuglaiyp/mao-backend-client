package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SceneInfo {
    // 场景状态：0游戏开始前初始化状态，1游戏开始状态，2游戏结束状态
    private int status = 0;
    private int onlineCnt = 0;
    ConcurrentHashMap<String, String> player2IP = new ConcurrentHashMap<>();
    private int totalPointCnt = 0;
}

package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStage {
    // 场景状态：0游戏开始前初始化状态，1游戏开始状态，2游戏结束状态
    private int status = 0;
    private volatile float progress = 0;
    private ConcurrentHashMap<String, Integer> player2Score = new ConcurrentHashMap<>();
}
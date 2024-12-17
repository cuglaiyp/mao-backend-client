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
    private boolean running = false;
    private float progress = 0;
    private ConcurrentHashMap<String, Integer> player2Score = new ConcurrentHashMap<>();
}
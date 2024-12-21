package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameInfo {
    private volatile float progress = 0;
    private ConcurrentHashMap<String, Integer> player2Score = new ConcurrentHashMap<>();

    public void reset() {
        progress = 0;
        player2Score.clear();
    }
}

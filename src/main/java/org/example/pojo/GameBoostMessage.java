package org.example.pojo;

import lombok.Data;

@Data
public class GameBoostMessage {

    private String player;
    private int boostCount;
}
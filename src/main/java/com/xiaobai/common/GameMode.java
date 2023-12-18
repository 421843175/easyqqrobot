package com.xiaobai.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobai
 * @date 2023/10/31-20:34
 */
@Getter
public enum GameMode {
    MSDL("魔兽大陆","/msdl"),
    XYX("小游戏","/xyx");  // 小游戏：彩票 井字棋 猜拳
    //TODO:增加新的游戏模式

    final String gameName;
    final String gameUrl;

    GameMode(String gameName,String gameUrl) {
        this.gameName = gameName;
        this.gameUrl = gameUrl;
    }

}

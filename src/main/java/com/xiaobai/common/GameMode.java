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
    JLDL("精灵大陆","/jldl"),
    CYJL("成语接龙","cyjl"),
    NHWC("你画我猜","/nhwc");
    //TODO:增加新的游戏模式

    final String gameName;
    final String gameUrl;

    GameMode(String gameName,String gameUrl) {
        this.gameName = gameName;
        this.gameUrl = gameUrl;
    }

}

package com.xiaobai.common;

import lombok.Getter;
import lombok.ToString;

/**
 * @author xiaobai
 * @date 2023/10/29-22:01
 */
@Getter
public enum RobotMode {
    CHAT("/聊天", "/chat", "聊天"),
    GAME("/游戏", "/game", "玩游戏"),
    TEST("/测试", "/test", "测试"),
    //TODO: 增加新的模式，走相应的controller
    TOOLS("/工具箱", "/tools", "工具箱");

    final String mode;
    final String modeUrl;
    final String modeName;

    RobotMode(String s, String modeUrl, String modeName) {
        this.mode = s;
        this.modeName = modeName;
        this.modeUrl = modeUrl;
    }

}

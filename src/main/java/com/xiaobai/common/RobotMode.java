package com.xiaobai.common;

import lombok.Getter;
import lombok.ToString;

/**
 * @author xiaobai
 * @date 2023/10/29-22:01
 */
@Getter
public enum RobotMode {
    CHAT("/chat"),
    GAME("/game"),
    TEST("/test"),
    NOTE("/note");

    final String mode;

    RobotMode(String s) {
        this.mode = s;
    }

}

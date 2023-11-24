package com.xiaobai.pojo.qqRobot.keyBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaobai
 * @date 2023/10/30-23:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    int type;
    Permission permission;
    int click_limit;
    String data;
    boolean at_bot_show_channel_list;
}

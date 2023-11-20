package com.xiaobai.pojo.qqRobot;

import com.xiaobai.pojo.qqRobot.keyBoard.InlineKeyboard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaobai
 * @date 2023/10/30-23:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageKeyboard {
    String id;
    InlineKeyboard content;
}

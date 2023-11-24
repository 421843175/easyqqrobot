package com.xiaobai.pojo.qqRobot.keyBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaobai
 * @date 2023/10/30-23:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InlineKeyboardRow {
    Button[] buttons;
}

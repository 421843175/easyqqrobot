package com.xiaobai.pojo.qqRobot.keyBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaobai
 * @date 2023/10/30-23:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Button {
    String id;
    RenderData render_data;
    Action action;
}

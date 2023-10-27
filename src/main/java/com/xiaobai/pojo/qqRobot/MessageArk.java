package com.xiaobai.pojo.qqRobot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @AUTHOR XiaoYang
 * @DATE 2023/10/23
 * @DESCRIPTION TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageArk {
    /**
     * ark模板id（需要先申请）
     */
    private int template_id;
    /**
     * kv值列表
     */
    private MessageArkKv[] kv;
}

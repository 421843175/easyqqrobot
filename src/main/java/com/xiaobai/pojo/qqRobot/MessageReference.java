package com.xiaobai.pojo.qqRobot;

import lombok.Data;

/**
 * @author xiaobai
 * @date 2023/10/23-17:36
 */
@Data
public class MessageReference {
    private String message_id;
    private boolean ignore_get_message_error = false;
}

package com.xiaobai.dto;

import com.xiaobai.pojo.qqRobot.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiaobai
 * @date 2023/10/28-23:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageDto extends Message {

    String forwardUrl;

    String targetUrl;
}

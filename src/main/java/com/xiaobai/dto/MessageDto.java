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
    //来源id
    String srcId;

    String forwardUrl;

    String targetUrl;

    @Override
    public String toString() {
        return "MessageDto{" + super.toString() +
                "forwardUrl='" + forwardUrl + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                '}';
    }
}

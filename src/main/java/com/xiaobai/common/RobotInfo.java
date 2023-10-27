package com.xiaobai.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xiaobai
 * @date 2023/10/24-14:00
 */

@Component
@Data
public class RobotInfo {
    @Value("${robot.appId}")
    private  String appId;

    @Value("${robot.token}")
    private  String token;

    @Value("${robot.clientSecret}")
    private  String clientSecret;
}

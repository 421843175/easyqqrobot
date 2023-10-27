package com.xiaobai.common;

import org.springframework.stereotype.Component;

/**
 * @author xiaobai
 * @date 2023/10/23-16:44
 */
@Component
public class BaseVar {
    public static String token = null;
    public static String heartbeatInterval = null;
    public static String finalMessageID = null;
    public static String sessionID = null;
    public volatile static String sparkMessage = null;


    public static String WS_URL = null;
    public static final String LOCAL_URL = "http://localhost:8080";
    public static final String TOKEN_URL = "https://bots.qq.com/app/getAppAccessToken";
    public static final String BASE_URL = "https://api.sgroup.qq.com";
    public static final String GATEWAY_URL = "/gateway";

}

package com.xiaobai.common;

import com.xiaobai.pojo.Mode;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaobai
 * @date 2023/10/23-16:44
 */
public class BaseVar {
    /**
     * 机器人基本参数
     */
    public static String token = null;
    public static String heartbeatInterval = null;
    public static String finalMessageID = null;
    public static String sessionID = null;

    /**
     *机器人模式
     */
    //TODO:
    public static ConcurrentHashMap<String, Mode> curMode = new ConcurrentHashMap<>();




    public volatile static String sparkMessage = null;
    public volatile static boolean sparkFlag = false;


    public static String WS_URL = null;
    public static final String LOCAL_URL = "http://localhost:8091";
    public static final String TOKEN_URL = "https://bots.qq.com/app/getAppAccessToken";
    public static final String BASE_URL = "https://api.sgroup.qq.com";
    public static final String GATEWAY_URL = "/gateway";


}

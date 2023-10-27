package com.xiaobai.service;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.pojo.qqRobot.Payload;
import com.xiaobai.utils.AuthorizationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobai
 * @date 2023/10/22-17:19
 */
@Service
@Slf4j
public class RobotService {

    @Autowired
    private RobotInfo robotInfo;

    @Value("${robot.intents}")
    private Integer intents;


    @Scheduled(cron = "0 0 */2 * * ?")
    public void flushToken(){
        log.info("获取凭证");
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(robotInfo));
        AuthorizationUtil.getToken(jsonObject);
    }

    /**
     * {
     *   "op": 2,
     *   "d": {
     *     "token": "token string",
     *     "intents": 513,
     *     "shard": [0, 4],
     *     "properties": {
     *       "$os": "linux",
     *       "$browser": "my_library",
     *       "$device": "my_library"
     *     }
     *   }
     * }
     */
    public String login() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", "QQBot " + BaseVar.token);
        map.put("intents", intents);
        map.put("shard", new int[]{0, 1});
        Payload payload = new Payload(2, map);
        return JSONObject.toJSONString(payload);
    }

}

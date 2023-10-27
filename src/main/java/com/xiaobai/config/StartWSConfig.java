package com.xiaobai.config;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.utils.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author xiaobai
 * @date 2023/10/24-15:33
 */
@Component
public class StartWSConfig implements ApplicationRunner {

    @Autowired
    RobotInfo robotInfo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String jsonString = JSONObject.toJSONString(robotInfo);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        AuthorizationUtil.getToken(jsonObject);



        AuthorizationUtil.getQQWSClient(robotInfo.getAppId());
    }
}

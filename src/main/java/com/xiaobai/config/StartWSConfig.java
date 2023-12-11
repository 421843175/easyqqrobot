package com.xiaobai.config;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.utils.AuthorizationUtil;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author xiaobai
 * @date 2023/10/24-15:33
 */
@Component
//ApplicationRunner 它是Spring Boot中用于在Spring应用程序启动完成后执行特定任务的接口。
public class StartWSConfig implements ApplicationRunner {

    @Autowired
    RobotInfo robotInfo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String jsonString = JSONObject.toJSONString(robotInfo);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        AuthorizationUtil.getToken(jsonObject);

        robotInfo.setHeaders(new Header[]{
                new BasicHeader("Authorization", "QQBot " + BaseVar.token),
                new BasicHeader("X-Union-Appid", robotInfo.getAppId())
        });


        AuthorizationUtil.getQQWSClient(robotInfo.getAppId());
    }
}

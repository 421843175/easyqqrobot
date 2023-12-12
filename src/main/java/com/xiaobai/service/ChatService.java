package com.xiaobai.service;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.common.SparkInfo;
import com.xiaobai.dto.MessageDto;
import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.pojo.qqRobot.MessageReference;
import com.xiaobai.utils.AuthorizationUtil;
import com.xiaobai.utils.HttpUtil;
import com.xiaobai.utils.MessageUtil;
import org.apache.logging.log4j.util.Strings;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xiaobai
 * @date 2023/10/23-23:20
 */
@Service
public class ChatService {

    final SparkInfo sparkInfo;

    final RobotInfo robotInfo;

    @Autowired
    public ChatService(RobotInfo robotInfo, SparkInfo sparkInfo) {
        this.robotInfo = robotInfo;
        this.sparkInfo = sparkInfo;
    }

    public void chat(MessageDto message) {
        try {
            WebSocketClient sparkWSClient = AuthorizationUtil.getSparkWSClient(sparkInfo.getHostUrl(), sparkInfo.getApiKey(), sparkInfo.getApiSecret());
            sparkWSClient.connect();

            do {
                if (BaseVar.sparkFlag) {
                    Map<String, Object> map = MessageUtil.buildSparkParam(sparkInfo.getAppId(),
                            message.getAuthor().getId(),
                            message.getContent().replaceAll("<@!.*?>", "").trim(),
                            sparkInfo.getDomain());
                    String sparkParam = JSONObject.toJSONString(map);
                    sparkWSClient.send(sparkParam);
                    BaseVar.sparkFlag = false;
                }
            } while (Strings.isBlank(BaseVar.sparkMessage));

            sparkWSClient.close(1000, "");


            JSONObject param = new JSONObject();
            StringBuilder builder = new StringBuilder("\n");

            param.put("msg_id", message.getId());
            builder.append(BaseVar.sparkMessage);
            //引用消息
            param.put("content", builder.toString());
            MessageReference messageReference = new MessageReference();
            messageReference.setMessage_id(message.getId());
            param.put("message_reference", messageReference);

            HttpUtil.executeRequest(
                    message.getTargetUrl(),
                    HttpMethod.POST,
                    param,
                    robotInfo.getHeaders()
            );


            BaseVar.sparkMessage = null;
            BaseVar.curMode = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}

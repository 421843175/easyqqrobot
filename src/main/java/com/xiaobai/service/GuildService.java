package com.xiaobai.service;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.common.SparkInfo;
import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.pojo.qqRobot.MessageReference;
import com.xiaobai.utils.AuthorizationUtil;
import com.xiaobai.utils.HttpUtil;
import com.xiaobai.utils.MessageUtil;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
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
public class GuildService {

    final SparkInfo sparkInfo;

    final RobotInfo robotInfo;

    @Autowired
    public GuildService(RobotInfo robotInfo, SparkInfo sparkInfo) {
        this.robotInfo = robotInfo;
        this.sparkInfo = sparkInfo;
    }

    public void channel(Message message) {
        try {
            WebSocketClient sparkWSClient = AuthorizationUtil.getSparkWSClient(sparkInfo.getHostUrl(), sparkInfo.getApiKey(), sparkInfo.getApiSecret());
            sparkWSClient.connect();

            do {
                if (BaseVar.sparkFlag) {
                    Map<String, Object> map = MessageUtil.buildSparkParam(sparkInfo.getAppId(),
                            message.getAuthor().getId(),
                            message.getContent().replace("<@!.*?>", "").trim(),
                            sparkInfo.getDomain());
                    String sparkParam = JSONObject.toJSONString(map);
                    sparkWSClient.send(sparkParam);
                    BaseVar.sparkFlag = false;
                }
            } while (Strings.isBlank(BaseVar.sparkMessage));

            sparkWSClient.close(1000, "");

            Header[] headers = new Header[2];
            headers[0] = new BasicHeader("Authorization", "QQBot " + BaseVar.token);
            headers[1] = new BasicHeader("X-Union-Appid", robotInfo.getAppId());

            Integer answerLength = robotInfo.getAnswerLength();

            if (BaseVar.sparkMessage.length() > answerLength) {
                int index = BaseVar.sparkMessage.length() / answerLength;
                int i = 0;
                String answer;
                while (i++ <= index) {
                    if (i > index) {
                        answer = BaseVar.sparkMessage.substring(answerLength * index);
                    } else {
                        answer = BaseVar.sparkMessage.substring(answerLength * (i - 1), answerLength * i);
                    }

                    JSONObject param = new JSONObject();
                    if (i == 1) {
                        answer = "<@!" + message.getAuthor().getId() + ">\n" + answer;
                        //引用消息
                        MessageReference messageReference = new MessageReference();
                        messageReference.setMessage_id(message.getId());
                        param.put("message_reference", messageReference);
                    }


                    param.put("content", answer);
                    param.put("msg_id", message.getId());

                    HttpUtil.executeRequest(
                            BaseVar.BASE_URL + "/channels/" + message.getChannel_id() + "/messages",
                            HttpMethod.POST,
                            param,
                            headers
                    );
                }
            }else {
                JSONObject param = new JSONObject();
                StringBuilder builder = new StringBuilder("<@!" + message.getAuthor().getId() + ">\n");


                param.put("msg_id", message.getId());
                builder.append(BaseVar.sparkMessage);
                //引用消息
                param.put("content", builder.toString());
                MessageReference messageReference = new MessageReference();
                messageReference.setMessage_id(message.getId());
                param.put("message_reference", messageReference);

                HttpUtil.executeRequest(
                        BaseVar.BASE_URL + "/channels/" + message.getChannel_id() + "/messages",
                        HttpMethod.POST,
                        param,
                        headers
                );
            }

            BaseVar.sparkMessage = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}

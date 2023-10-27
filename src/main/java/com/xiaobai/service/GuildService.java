package com.xiaobai.service;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
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
public class GuildService{

    @Autowired
    SparkInfo sparkInfo;

    public void channel(Message message) {
        try {
            WebSocketClient sparkWSClient = AuthorizationUtil.getSparkWSClient(sparkInfo.getHostUrl(), sparkInfo.getApiKey(), sparkInfo.getApiSecret());
            sparkWSClient.connect();
            Thread.sleep(200);
            Map<String, Object> map = MessageUtil.buildSparkParam(sparkInfo.getAppId(),
                    message.getAuthor().getId(),
                    message.getContent().replace("<@!13224066619675576179>", "").trim(),
                    sparkInfo.getDomain());



            String sparkParam = JSONObject.toJSONString(map);

            sparkWSClient.send(sparkParam);
            while (true){
                Thread.sleep(200);
                if(Strings.isNotBlank(BaseVar.sparkMessage)){
                    break;
                }
            }
            sparkWSClient.close(1000, "");

            Header[] headers = new Header[2];
            headers[0] = new BasicHeader("Authorization", "QQBot " + BaseVar.token);
            headers[1] = new BasicHeader("X-Union-Appid", "102071706");

            JSONObject param = new JSONObject();

            param.put("content",BaseVar.sparkMessage);
            MessageReference messageReference = new MessageReference();
            messageReference.setMessage_id(message.getId());
            param.put("message_reference",messageReference);
            param.put("msg_id",message.getId());
            param.put("msg_type",0);
            param.put("timestamp",(int)System.currentTimeMillis()/1000);

            HttpUtil.executeRequest(
                    BaseVar.BASE_URL + "/channels/" + message.getChannel_id() + "/messages",
                    HttpMethod.POST,
                    param,
                    headers
            );

            BaseVar.sparkMessage = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




    }
}

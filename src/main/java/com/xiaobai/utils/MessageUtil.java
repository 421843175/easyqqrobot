package com.xiaobai.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.dto.MessageDto;
import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.pojo.qqRobot.Payload;
import com.xiaobai.pojo.qqRobot.User;
import com.xiaobai.pojo.spark.SparkRoleContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpMethod;

import java.util.*;

/**
 * @author xiaobai
 * @date 2023/10/23-10:33
 */
@Slf4j
public class MessageUtil {

    private static final Map<String, String> opCode;

    static {
        opCode = new HashMap<>();
        opCode.put("0", "服务端进行消息推送");
        opCode.put("1", "客户端或服务端发送心跳");
        opCode.put("2", "客户端发送鉴权");
        opCode.put("6", "客户端恢复连接");
        opCode.put("7", "服务端通知客户端重新连接");
        opCode.put("9", " identify 或 resume ，参数有误");
        opCode.put("10", "hello");
        opCode.put("11", "心跳成功");
        opCode.put("12", "机器人收到消息");
    }


    public static Map<String, Object> parseQQPaylod(String message) {
        Payload payload = JSONObject.parseObject(message, Payload.class);
        Map<String, Object> map = new HashMap<>();


        Integer op = payload.getOp();
        if (Strings.isNotBlank(payload.getS())) {
            BaseVar.finalMessageID = payload.getS();
        }


        if (op == 11) {
            return null;
        }
        if (op == 1) {
            log.info("服务端维持心跳");
            return null;
        }
        if (op == 7) {
            log.info("啊哦，要重新连接一下咯");
            map.put("arg", "close");
            return map;
        }
        if (op == 10) {
            BaseVar.heartbeatInterval = String.valueOf(JSONObject
                    .parseObject(payload.getD().toString())
                    .get("heartbeat_interval"));
            log.info("连接网关成功，机器人启动ing。心跳周期为{}ms，小心断线嗷", BaseVar.heartbeatInterval);

            map = HttpUtil.executeRequest(
                    BaseVar.LOCAL_URL + "/login",
                    HttpMethod.GET
            );
            return map;
        }
        if (op == 9) {
            log.info("请求参数异常");
            log.info("{}",message);
        }


        if (op == 0) {

            if ("READY".equals(payload.getT())) {
                map.put("arg", "keepAlive");
                BaseVar.sessionID = String.valueOf(
                        JSONObject
                        .parseObject(payload.getD().toString())
                        .get("session_id")
                );
                return map;
            }

            MessageDto messageDto = JSONObject.parseObject(payload.getD().toString(), MessageDto.class);
            if (Objects.isNull(messageDto)) {
                return null;
            }
            if (Objects.nonNull(messageDto.getContent())) {
                log.info("{}",messageDto);
                HttpUtil.executeRequest(
                        BaseVar.LOCAL_URL + "/router",
                        HttpMethod.POST,
                        (JSONObject) JSONObject.toJSON(messageDto)
                );
            }
        }
        return null;
    }

    public static Map<String, Object> buildSparkParam(String appid, String uid, String content,String domain) {
        JSONObject requestJson = new JSONObject();

        JSONObject header = new JSONObject();  // header参数
        header.put("app_id", appid);
        header.put("uid", uid);

        JSONObject parameter = new JSONObject(); // parameter参数
        JSONObject chat = new JSONObject();
        chat.put("domain", domain);
        chat.put("temperature", 0.5);
        chat.put("max_tokens", 4096);
        parameter.put("chat", chat);

        JSONObject payload = new JSONObject(); // payload参数
        JSONObject message = new JSONObject();
        JSONArray text = new JSONArray();

        /*// 历史问题获取
        if(historyList.size()>0){
            for(RoleContent tempRoleContent:historyList){
                text.add(JSON.toJSON(tempRoleContent));
            }
        }*/

        // 最新问题
        SparkRoleContent roleContent = new SparkRoleContent();
        roleContent.setRole("user");
        roleContent.setContent(content);
        text.add(JSON.toJSON(roleContent));


        message.put("text", text);
        payload.put("message", message);


        requestJson.put("header", header);
        requestJson.put("parameter", parameter);
        requestJson.put("payload", payload);

        return requestJson;
    }

    public static String buildTargetUrl(Message message){
        String targetUrl;
        if (Strings.isBlank(message.getChannel_id())){
            targetUrl = BaseVar.BASE_URL + "/v2/groups/" + message.getGroup_openid() + "/messages";
        }else {
            targetUrl = BaseVar.BASE_URL + "/channels/" + message.getChannel_id() + "/messages";
        }
        return targetUrl;
    }
}

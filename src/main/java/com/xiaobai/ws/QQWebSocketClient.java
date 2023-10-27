package com.xiaobai.ws;



import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.pojo.qqRobot.Payload;
import com.xiaobai.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

/**
 * @author xiaobai
 * @date 2023/10/22-10:41
 */

@Slf4j
public class QQWebSocketClient extends WebSocketClient {

    public QQWebSocketClient(URI serverUri) {
        super(serverUri);
    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("onOpen");
    }

    @Override
    public void onMessage(String message) {
        Map<String, Object> map = MessageUtil.parsePaylod(message);
        if(Objects.nonNull(map)){
            log.info("回调结果:{}",map);
            if(map.containsKey("arg")){
                try {
                    Method method = this.getClass().getMethod((String) map.get("arg"));
                    method.invoke(this);
                    return;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
            send(JSONObject.toJSONString(map));
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("onClose,重新连接中...");
        new Thread(new Reconnect(this)).start();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    public void keepAlive() {
        if (Strings.isNotBlank(BaseVar.heartbeatInterval)) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String param = JSONObject.toJSONString(new Payload(1, BaseVar.finalMessageID));
                    send(param);
                    log.info("维持心跳{}",param);
                }
            },0,Long.parseLong(BaseVar.heartbeatInterval));
        }
    }



    private class Reconnect implements Runnable{
        private WebSocketClient webSocketClient;

        public Reconnect(WebSocketClient webSocketClient) {
            this.webSocketClient = webSocketClient;
        }

        @Override
        public void run() {
            if (webSocketClient.isClosed()) {
                webSocketClient.reconnect();
                log.info("重连成功");


                Map<String,Object> map = new HashMap<>();
                map.put("token",BaseVar.token);
                map.put("session_id",BaseVar.sessionID);
                map.put("seq",BaseVar.finalMessageID);


                Payload payload = new Payload(6,map);
                webSocketClient.send(JSONObject.toJSONString(payload));


            }
        }
    }
}

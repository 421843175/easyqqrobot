package com.xiaobai.ws;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.pojo.spark.SparkResult;
import com.xiaobai.pojo.spark.SparkText;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.List;

/**
 * @author xiaobai
 * @date 2023/10/24-17:32
 */
@Slf4j
public class SparkWebSocketClient extends WebSocketClient {
    public SparkWebSocketClient(URI serverUri) {
        super(serverUri);
    }

     static StringBuilder  totalAnswer = new StringBuilder();

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("连接上星火大模型啦");
    }

    @Override
    public void onMessage(String s) {
        SparkResult sparkResult = JSONObject.parseObject(s, SparkResult.class);

        if (sparkResult.getHeader().getCode() != 0) {
            System.out.println("发生错误，错误码为：" + sparkResult.getHeader().getCode());
            System.out.println("本次请求的sid为：" + sparkResult.getHeader().getSid());
            this.close(1000, "");
        }
        List<SparkText> textList = sparkResult.getPayload().getChoices().getText();
        ;
        for (SparkText temp : textList) {
            totalAnswer.append(temp.getContent());
        }
        if (sparkResult.getHeader().getStatus() == 2) {
            BaseVar.sparkMessage = totalAnswer.toString();
            log.info("{}",totalAnswer);
            totalAnswer.setLength(0);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("星火大模型已断开");
    }

    @Override
    public void onError(Exception e) {

    }
}

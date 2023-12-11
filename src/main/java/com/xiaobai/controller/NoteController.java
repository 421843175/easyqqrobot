package com.xiaobai.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.pojo.qqRobot.MessageReference;
import com.xiaobai.utils.HttpUtil;
import com.xiaobai.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaobai
 * @date 2023/12/7-19:43
 */
@RestController
@RequestMapping("/note")
public class NoteController {

    //TODO:note模式的controller
    @Autowired
    RobotInfo robotInfo;
    @RequestMapping
    public void write(HttpServletRequest request) {
        Message message = (Message) request.getAttribute("message");

        String targetUrl = MessageUtil.buildTargetUrl(message);

        JSONObject param = new JSONObject();

        MessageReference messageReference = new MessageReference();
        messageReference.setMessage_id(message.getId());
        param.put("message_reference", messageReference);


        param.put("content", "正在写笔记");
        param.put("msg_id", message.getId());

        HttpUtil.executeRequest(
                targetUrl,
                HttpMethod.POST,
                param,
                robotInfo.getHeaders()
        );
        BaseVar.curMode = null;
    }
}

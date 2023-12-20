package com.xiaobai.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.common.RobotMode;
import com.xiaobai.dto.MessageDto;
import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.pojo.qqRobot.MessageReference;
import com.xiaobai.service.ToolsService;
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
@RequestMapping("/tools")
public class ToolsController {

    //TODO:tools模式的controller
    @Autowired
    RobotInfo robotInfo;

    @Autowired
    ToolsService toolsService;

    @RequestMapping
    public void toolsinfo(HttpServletRequest request) {
        MessageDto message = (MessageDto) request.getAttribute("message");

        JSONObject param = new JSONObject();

        MessageReference messageReference = new MessageReference();
        messageReference.setMessage_id(message.getId());
        param.put("message_reference", messageReference);

        if ("".equals(message.getContent())) {
            param.put("content", "\n工具箱:\n1.随机一言\n2.历史上的今天\n3.百度热搜\n退出请输入 退出工具箱");
        } else if(message.getContent().equals("退出工具箱")){
            BaseVar.curMode.get(message.getSrcId()).robotMode = null;
            param.put("content", "工具箱已退出");
        }else {
            param.put("content","\n" + toolsService.toolsinfo(message));
        }


        param.put("msg_id", message.getId());

        HttpUtil.executeRequest(
                message.getTargetUrl(),
                HttpMethod.POST,
                param,
                robotInfo.getHeaders()
        );

    }



}

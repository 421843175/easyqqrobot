package com.xiaobai.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.common.RobotMode;
import com.xiaobai.config.AnswerMethod;
import com.xiaobai.dto.MessageDto;
import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.utils.HttpUtil;
import com.xiaobai.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map.Entry;


/**
 * @author xiaobai
 * @date 2023/10/24-9:54
 */

@Controller
@Slf4j
public class IndexController {

    @Autowired
    RobotInfo robotInfo;

    @RequestMapping("/router")
    public String router(@RequestBody MessageDto message, HttpServletRequest request) {
        log.info("路由控制器");

        StringBuilder url = new StringBuilder();
        message.setContent(message.getContent()
                .replaceAll("<@!.*?>", "")
                .trim());
        //设置发送消息路径
        MessageUtil.buildTargetUrl(message);
        request.setAttribute("message", message);


        //设置了模式走设置的模式
        if (BaseVar.curMode != null) {
            url.append(BaseVar.curMode.getMode());
            if (BaseVar.gameMode != null) {
                url.append(BaseVar.gameMode.getGameUrl());
            }

            return "forward:" + url;
        }


        String mode = message.getContent().split(" ")[0];


        //没设置模式
        if (Arrays.stream(RobotMode.values()).anyMatch(robotMode -> {
            if (robotMode.getMode().equals(mode)) {
                BaseVar.curMode = robotMode;
                return true;
            }
            return false;
        })) {
            message.setForwardUrl(mode);
            message.setContent(message.getContent().replace(mode, ""));
        } else {
            JSONObject param = new JSONObject();

            param.put("content", "小白白不知道你要干什么，请通过指令控制我叭");
            param.put("msg_id", message.getId());

            HttpUtil.executeRequest(
                    message.getTargetUrl(),
                    HttpMethod.POST,
                    param,
                    robotInfo.getHeaders()
            );
            return null;
        }
        return "forward:" + message.getForwardUrl();
    }
}

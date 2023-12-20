package com.xiaobai.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.common.RobotMode;
import com.xiaobai.config.AnswerMethod;
import com.xiaobai.dto.MessageDto;
import com.xiaobai.pojo.Mode;
import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.service.RobotService;
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

    @Autowired
    RobotService robotService;


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
        if (BaseVar.curMode.get(message.getSrcId()) != null) {
            url.append(BaseVar.curMode.get(message.getSrcId()).getRobotMode().getModeUrl());
            if (BaseVar.curMode.get(message.getSrcId()).getGameMode() != null) {
                url.append(BaseVar.curMode.get(message.getSrcId()).getGameMode().getGameUrl());
            }

            return "forward:" + url;
        }


        String mode = message.getContent().split(" ")[0];


        //没设置模式
        if (Arrays.stream(RobotMode.values()).anyMatch(robotMode -> {
            if (robotMode.getMode().equals(mode)) {
                BaseVar.curMode.put(message.getSrcId(),new Mode(robotMode));
                return true;
            }
            return false;
        })) {
            message.setForwardUrl(BaseVar.curMode.get(message.getSrcId()).getRobotMode().getModeUrl());
            message.setContent(message.getContent().replace(mode, ""));
        } else {
            JSONObject param = new JSONObject();
            StringBuilder content = new StringBuilder("\n");
            if("菜单".equals(message.getContent())){
                for (RobotMode item : RobotMode.values()) {
                    if("测试".equals(item.getModeName())){
                        continue;
                    }
                    content.append(item.getMode()).append("\n");
                }
            }else {
                content.append("艾特我要说点我听得懂的话哦~\n比如:艾特我说“/tools”\n更多指令艾特我说‘菜单’哦\"");
            }

            param.put("content", content);
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

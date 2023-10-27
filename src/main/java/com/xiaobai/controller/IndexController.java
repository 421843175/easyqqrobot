package com.xiaobai.controller;

import com.xiaobai.pojo.qqRobot.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * @author xiaobai
 * @date 2023/10/24-9:54
 */

@Controller
@Slf4j
public class IndexController {
    @RequestMapping("/router")
    public String router(@RequestBody Message message, HttpServletRequest request) throws IOException {
        StringBuilder url = new StringBuilder();
        log.info("路由控制器");
        if(message.getChannel_id()!=null){
            url.append("/guild").append("/channel");
        }

        request.setAttribute("message",message);

        return "forward:" + url;
    }
}

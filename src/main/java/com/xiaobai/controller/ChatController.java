package com.xiaobai.controller;

import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaobai
 * @date 2023/10/24-10:18
 */

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    /**
     * 文字子频道
     */
    @RequestMapping
    public void channel(HttpServletRequest request){
        Message message = (Message) request.getAttribute("message");
        chatService.channel(message);
    }

}

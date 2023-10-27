package com.xiaobai.controller;

import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.service.GuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author xiaobai
 * @date 2023/10/24-10:18
 */

@RestController
@RequestMapping("/guild")
public class GuildController {
    @Autowired
    private GuildService guildService;
    /**
     * 文字子频道
     */
    @RequestMapping("/channel")
    public void channel(@RequestBody Message message){

        guildService.channel(message);
    }

}

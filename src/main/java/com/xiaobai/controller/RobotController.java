package com.xiaobai.controller;

import com.xiaobai.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaobai
 * @date 2023/10/17-20:17
 */
@RestController
public class RobotController {
    @Autowired
    private RobotService robotService;


    @GetMapping("/login")
    public String login() {
        return robotService.login();
    }

}

package com.xiaobai.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.GameMode;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.service.JldlService;
import com.xiaobai.utils.HttpUtil;
import com.xiaobai.utils.MessageUtil;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author xiaobai
 * @date 2023/10/31-10:06
 */
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    RobotInfo robotInfo;

    @Autowired
    JldlService jldlService;

    static Map<String, GameMode> gameName = new ConcurrentHashMap<>();

    static {
        for (GameMode value : GameMode.values()) {
            JSONObject game = JSONObject.parseObject(value.getGame());
            for (String s : game.keySet()) {
                gameName.put(s, value);
            }
        }
    }

    @RequestMapping
    public void start(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        JSONObject param = new JSONObject();
        StringBuilder builder = new StringBuilder();
        Message message = (Message) request.getAttribute("message");

        String targetUrl = MessageUtil.buildTargetUrl(message);

        if ("退出游戏".equals(message.getContent())) {
            builder.append("游戏模式已退出");
            BaseVar.curMode = null;
        } else if (gameName.containsKey(message.getContent())) {
            BaseVar.gameMode = gameName.get(message.getContent());
            builder.append(message.getContent()).append("已开始\n");
            builder.append("发送“菜单”");
        } else {

            for (String s : gameName.keySet()) {
                builder.append(s);
                builder.append("\n");
            }


            builder.append("请@我说出想玩的游戏\n");
            builder.append("其他游戏还在开发中，会陆续上架。如果想退出游戏模式，请发送“退出游戏”");


        }

        param.put("content", builder.toString());
        param.put("msg_id", message.getId());

        HttpUtil.executeRequest(
                targetUrl,
                HttpMethod.POST,
                param,
                robotInfo.getHeaders());

    }

    @RequestMapping("/jldl")
    public void jldl(HttpServletRequest request) {
        JSONObject param = new JSONObject();
        String image = null;
        String content = null;

        Message message = (Message) request.getAttribute("message");
        String targetUrl = MessageUtil.buildTargetUrl(message);


        if ("返回菜单".equals(message.getContent())) {
            content = "精灵大陆已退出";
            BaseVar.gameMode = null;
        } else if("菜单".equals(message.getContent())){
            StringBuilder builder = new StringBuilder();

            builder.append("精灵大陆\n");
            builder.append("1.签到\t\t2.我的背包\n\n");
            builder.append("想返回主菜单吗，请发送“返回菜单”");
            image = "https://img1.baidu.com/it/u=198137113,2555698160&fm=253&fmt=auto&app=138&f=PNG?w=562&h=468";
            content = builder.toString();
        }else {
            content = jldlService.buildAnswer(message);
            image = jldlService.getImage(content);
        }
        //http://img.faloo.com/Novel/498x705/0/271/000271725.jpg
        if(image != null){
            param.put("image",image);
        }
        if(content.isEmpty()){
            content = "不理解您的指令呢";
        }

        param.put("content", content);
        param.put("msg_id", message.getId());

        HttpUtil.executeRequest(
                targetUrl,
                HttpMethod.POST,
                param,
                robotInfo.getHeaders());

    }
}

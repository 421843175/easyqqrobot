package com.xiaobai.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.GameMode;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.dto.MessageDto;
import com.xiaobai.service.game.msdl.MsdlService;
import com.xiaobai.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    MsdlService msdlService;

    static Map<String, GameMode> gameName = new ConcurrentHashMap<>();

    static {
        for (GameMode game : GameMode.values()) {
            gameName.put(game.getGameName(), game);
        }
    }

    @RequestMapping
    public void start(HttpServletRequest request) {

        JSONObject param = new JSONObject();
        StringBuilder builder = new StringBuilder("\n");
        MessageDto message = (MessageDto) request.getAttribute("message");

        if ("退出游戏".equals(message.getContent())) {
            builder.append("游戏模式已退出\n还想找我玩的话。记得艾特我说“/游戏”");
            BaseVar.curMode.get(message.getSrcId()).robotMode = null;
        } else if (gameName.containsKey(message.getContent())) {
            BaseVar.curMode.get(message.getSrcId()).setGameMode(gameName.get(message.getContent()));
            builder.append("已加载游戏\t")
                    .append(BaseVar.curMode.get(message.getSrcId()).gameMode.getGameName())
                    .append("\n")
                    .append("准备好的话，艾特我发送“开始游戏”吧~");
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
                message.getTargetUrl(),
                HttpMethod.POST,
                param,
                robotInfo.getHeaders());

    }

    @RequestMapping("/msdl")
    public void msdl(HttpServletRequest request) {
        MessageDto message = (MessageDto) request.getAttribute("message");
        msdlService.playGame(message);

    }
}

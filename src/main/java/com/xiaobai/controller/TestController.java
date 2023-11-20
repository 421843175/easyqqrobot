package com.xiaobai.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.pojo.qqRobot.MessageReference;
import com.xiaobai.pojo.qqRobot.keyBoard.*;
import com.xiaobai.utils.HttpUtil;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xiaobai
 * @date 2023/10/30-23:31
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    RobotInfo robotInfo;

    @RequestMapping
    public void test(HttpServletRequest request){
        JSONObject param = new JSONObject();
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Authorization", "QQBot " + BaseVar.token);
        headers[1] = new BasicHeader("X-Union-Appid", robotInfo.getAppId());

        Message message = (Message) request.getAttribute("message");

        param.put("msg_id", message.getId());

        InlineKeyboard keyboard = new InlineKeyboard(
                new InlineKeyboardRow[]{
                        new InlineKeyboardRow(
                                new Button[]{
                                        new Button("1",new RenderData(
                                                "测试按钮","已点击",1
                                        ),new Action(1,new Permission(
                                                1,null,null
                                        ),1,"按钮已被点击",false))
                                }
                        )
                }
        ,"102071706");
        System.out.println(JSONObject.toJSON(keyboard));



        //引用消息
        //param.put("content", "现在是测试模式哦");

        Map map = HttpUtil.executeRequest(
                BaseVar.BASE_URL + "/channels/" + message.getChannel_id() + "/messages",
                HttpMethod.POST,
                (JSONObject) JSONObject.toJSON(keyboard),
                headers
        );
        System.out.println(map);
    }

}

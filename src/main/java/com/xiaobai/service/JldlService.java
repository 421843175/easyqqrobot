package com.xiaobai.service;

import com.xiaobai.pojo.qqRobot.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiaobai
 * @date 2023/11/1-23:09
 */
@Service
public class JldlService {

    @Autowired
    private PointsService pointsService;

    @Autowired
    private SignService signService;

    public String buildAnswer(Message message) {
        String mess = message.getContent().replace("/game ", "");
        StringBuilder builder = new StringBuilder();
        switch (mess) {
            case "签到": {
                int resultday = signService.toSign(message.getAuthor().getId());
                if (resultday!=-1) {
                    //数据写入数据库
                    Integer points= (int) (Math.random() * 100) + 20;
                    pointsService.addPoints(message.getAuthor().getId(), points, 0);

                    builder.append("签到成功\n").append("你已连续签到").append(resultday).append("天");

                    builder.append("获得").append(points + "代币\n");

                } else {
                    builder.append("你今天已经签到过了！\n不要重复签到哦\n");
                    break;
                }
                //这个地方就不加break了
            }
            case "我的积分":{
                builder.append("你的积分为：").append(pointsService.getPoints(message.getAuthor().getId()));
            }

        }
        return builder.toString();
    }

    public String getImage(String content) {
        if (content.contains("签到成功")) {
            return "https://hbimg.huabanimg.com/93440af84573af4b3976b145b49c77c230b95ed717d68-mneEfX_fw658";
        } else if (content.contains("签到过了")) {
            return "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2Fe56773fd-2b73-4078-a281-7e76b25c1ccf%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1701444335&t=a12e03cceaf49e9dc00685aa871356eb";
        }
        return null;
    }
}

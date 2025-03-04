package com.xiaobai.service.game.msdl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaobai.common.BaseVar;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.dto.MessageDto;
import com.xiaobai.mapping.AnswerMapper;
import com.xiaobai.pojo.entity.AnswerBean;
import com.xiaobai.pojo.qqRobot.Message;
import com.xiaobai.pojo.qqRobot.MessageReference;
import com.xiaobai.service.game.GameService;
import com.xiaobai.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author xiaobai
 * @date 2023/11/1-23:09
 */
@Service
public class MsdlService implements GameService {

    @Autowired
    private RobotInfo robotInfo;

    @Autowired
    private PointsService pointsService;

    @Autowired
    private SignService signService;

    @Autowired
    ShopBagService sbs;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private MonsterService monsterService;

    public String buildAnswer(MessageDto message) {
        String content = message.getContent();
        StringBuilder builder = new StringBuilder();
        switch (content) {
            case "签到": {
                int resultday = signService.toSign(message.getAuthor().getId());
                if (resultday != -1) {
                    //数据写入数据库
                    Integer points = (int) (Math.random() * 100) + 20;
                    pointsService.addPoints(message.getAuthor().getId(), points, 0);

                    builder.append("签到成功\n").append("你已连续签到").append(resultday).append("天\n");

                    builder.append("获得").append(points + "积分\n");

                } else {
                    builder.append("你今天已经签到过了！\n不要重复签到哦\n");
                    break;
                }
                //这个地方就不加break了
            }
            case "我的积分": {
                builder.append("你的积分为：").append(pointsService.getPoints(message.getAuthor().getId()));
                break;
            }
            case "我的背包":{
                builder.append(sbs.getBag(message.getAuthor().getId()));
                break;
            }
            case "商城":{
                builder.append(sbs.getShop());
                break;
            }
            case "我的权限":{
                builder.append(pointsService.getPower(message.getAuthor().getId()));
                break;
            }
            case "怪物挑战":{
                builder.append(monsterService.getMonster());
                break;
            }
            case "退出挑战":{
                if(!BaseVar.curMode.get(message.getSrcId()).ischallengeBoss){
                    builder.append("您还没有开启挑战哦");
                }else {
                    BaseVar.curMode.get(message.getSrcId()).ischallengeBoss=false;
                    builder.append("退出成功!");
                }

            }
            case "打劫":{
                if(!BaseVar.curMode.get(message.getSrcId()).ischallengeBoss){
                    builder.append("您还没有开启挑战哦");
                }else {
                    BaseVar.curMode.get(message.getSrcId()).ischallengeBoss=false;
                    builder.append("退出成功!");
                }

            }

            default:
                if(content.startsWith("购买")){
                    String itemToBuy = content.substring(3); // 去除前面的"购买 "，得到购买的物品名称
                    builder.append(sbs.buy(message.getAuthor().getId(),itemToBuy));
                }
                else if(content.startsWith("查看")){
                    String itemTo = content.substring(3); // 去除前面的"购买 "，得到购买的物品名称
                    builder.append(sbs.see(itemTo));
                }
//                怪物挑战
                else if(content.startsWith("挑战")){
                    if(BaseVar.curMode.get(message.getSrcId()).ischallengeBoss){
                        builder.append("您正在挑战中哦！");
                    }else{
                        String boss = content.substring(3); // 去除前面的"挑战 "，得到挑战怪物名称
                        builder.append(monsterService.startAttackBoss(boss));
                        BaseVar.curMode.get(message.getSrcId()).ischallengeBoss=true;
                    }

                }
                else if(content.startsWith("攻击") || content.startsWith("投掷") ||content.startsWith("使用")){
                    if(BaseVar.curMode.get(message.getSrcId()).ischallengeBoss){
                        builder.append(monsterService.attackBoss(message.getAuthor().getId(),message.getSrcId(),content));
                    }else {
                        builder.append("您还没有开启挑战哦");
                    }
                }

                else {
                    builder.append("不理解您的指令哦,请发送“菜单”查看指令\n若要退出魔兽大陆 请发送\"返回菜单\"");
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

    @Override
    public void playGame(MessageDto message) {
        JSONObject param = new JSONObject();
        String image = null;
        StringBuilder content = new StringBuilder("\n" + BaseVar.curMode.get(message.getSrcId()).getGameMode().getGameName() + "\n");

        AnswerBean answerBean = answerMapper.selectOne(Wrappers.<AnswerBean>lambdaQuery()
                .select(AnswerBean::getAnswer)
                .eq(AnswerBean::getKeyword, message.getContent())
                .and(wrapper ->
                        wrapper.eq(AnswerBean::getGame, BaseVar.curMode.get(message.getSrcId()).getGameMode().getGameName())
                                .or()
                                .eq(AnswerBean::getGame, "ALL")
                )
        );
        if (Objects.nonNull(answerBean)) {
            if ("已退出".equals(answerBean.getAnswer())) {
                content.append(BaseVar.curMode.get(message.getSrcId()).getGameMode().getGameName()).append(answerBean.getAnswer());
                BaseVar.curMode.get(message.getSrcId()).setGameMode(null);
            }else {
                content.append(answerBean.getAnswer());
            }
        }else {
            content.append(buildAnswer(message));
        }
        //构建图片
        /*if(image != null){
            param.put("image",image);
        }*/

        MessageReference messageReference = new MessageReference();
        messageReference.setMessage_id(message.getId());
        param.put("message_reference", messageReference);

        param.put("content", content);
        param.put("msg_id", message.getId());

        //修改id

        HttpUtil.executeRequest(
                message.getTargetUrl(),
                HttpMethod.POST,
                param,
                robotInfo.getHeaders());
    }


}

package com.xiaobai.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaobai.common.BaseVar;
import com.xiaobai.mapping.BagMapper;
import com.xiaobai.mapping.MonsterMapper;
import com.xiaobai.mapping.ShopMapper;
import com.xiaobai.mapping.UserMapper;
import com.xiaobai.pojo.BufferBean;
import com.xiaobai.pojo.entity.BagBean;
import com.xiaobai.pojo.entity.MonsterBean;
import com.xiaobai.pojo.entity.ShopBean;
import com.xiaobai.pojo.entity.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MonsterService {
    @Autowired
    private MonsterMapper monsterMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BagMapper bagMapper;
    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopBagService shopBagService;


    private int health;
    private LinkedHashMap<String,Integer> playheath;  //玩家生命
    private LinkedHashMap<String,Integer> playDefense;  //玩家防御
    private int playattack=2;  //玩家攻击
    private HashMap<String,Integer> skills;
    private int reward=0;
    private MonsterBean monsterBean;


    public String getMonster(){
        StringBuffer sb=new StringBuffer();
        List<MonsterBean> monsterBeans = monsterMapper.selectList(null);
        for (MonsterBean monsterBean : monsterBeans) {
            sb.append(monsterBean.getName()+":(掉落:"+monsterBean.getReward()+"积分|冷却时间:"+monsterBean.getCool()+"分钟)\n"+monsterBean.getInfo()+"\n");
        }
        return sb.toString();
    }

    public String startAttackBoss(String bossname){
        QueryWrapper<MonsterBean> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",bossname);
         monsterBean = monsterMapper.selectOne(queryWrapper);
        if(monsterBean==null)
            return "您输入的怪物名有误";

        //初始化
        if(monsterBean.getSuccessDate()!=null){
            if(!isDateBeforeNMinutes(monsterBean.getSuccessDate(),monsterBean.getCool())){
                return "怪物重生还在冷却 等会儿再过来打吧";
            }
        }

        playheath=new LinkedHashMap<>();  //玩家生命
        playattack=2;  //玩家攻击
        reward=monsterBean.getReward();
        playDefense=new LinkedHashMap<>();

        String buffer = monsterBean.getBuffer();
        JSONObject jsonObject = JSONObject.parseObject(buffer);
        health=jsonObject.getInteger("health");
        skills=new HashMap<>();
        JSONArray ski = jsonObject.getJSONArray("skills");
        for(int i=0;i<ski.size();i++) {
            skills.put(ski.getJSONObject(i).getString("name"),ski.getJSONObject(i).getInteger("damage"));
        }

        return "挑战开始!(请使用背包的武器 使用方式 攻击/投掷  例如:攻击 匕首\n消耗类物品 例如:使用 绷带\n退出请输入 退出挑战)";

    }

    public String attackBoss(String userid,String code){
        StringBuffer sb=new StringBuffer();
        int keyPosition = getKeyPosition(playheath, userid);
        if(keyPosition!=-1){
            if(playheath.get(getKeyAtIndex(playheath,keyPosition))<0){
                return "系统:对不起 玩家"+keyPosition+"您已被怪物攻击倒下";
            }
          sb.append("玩家"+keyPosition+":");
      }else {
            playheath.put(userid,100);
            sb.append("玩家"+(playheath.size()-1)+":");
        }

        boolean isattack=false;
        BufferBean resultattack=null;
        //玩家攻击 触发怪物技能
        if(code.startsWith("攻击")){
            String item[] = code.split(" "); // 得到物品名称
            if(item.length==1){
                health-=2;
                sb.append("进行了一次普通攻击，怪物生命减少2");
                isattack=true;
            }else{
                resultattack=usething(userid,item[1],0);
                sb.append("您成功用物品攻击 "+item[1]+" ");
            }
        }else if(code.startsWith("投掷")){
            String item = code.substring(3); //得到物品名称
            sb.append("您成功投掷物品 "+item+" ");
            resultattack=  usething(userid,item,1);

        }else if(code.startsWith("使用")){
            String item = code.substring(3); // 得到物品名称
            sb.append("您成功使用物品 "+item+" ");
            resultattack=usething(userid,item,2);
        }
        if(resultattack==null &&    !isattack )
        {
            return "物品不存在或物品使用方式有问题!";
        }
        //回血
        if(resultattack!=null){
            playheath.put(userid,resultattack.getReturnHeath()+playheath.get(userid));
            //回血
            if(playheath.get(userid)>=100) playheath.put(userid,100);
            if(resultattack.getReturnHeath()>0){
                sb.append("回复生命值 目前您的血量是:"+playheath.get(userid));
                return sb.toString();
            }
            //增血
            playheath.put(userid,resultattack.getAddHeath()+playheath.get(userid));
            if(resultattack.getAddHeath()>0){
                sb.append("增加血量 "+resultattack.getAddHeath()+"点");
                return sb.toString();
            }
            //攻击
            health-=resultattack.getAddAttack();
            if(resultattack.getAddAttack()>0) sb.append("攻击伤害 "+resultattack.getAddAttack()+"点");
            //增防
            if(resultattack.getAddDefense()>0)
                if(playDefense.containsKey(userid))
                playDefense.put(userid,resultattack.getAddDefense()+playDefense.get(userid));
                else
                    playDefense.put(userid,resultattack.getAddDefense());
            if(resultattack.getAddDefense()>0) sb.append("增加防御 "+resultattack.getAddDefense()+"点");
        }


        sb.append("\n");
        //怪物还有血吗
        if(health>0){
            sb.append(bossatack());

        }else{

            //平分积分
            int h=0;

            //删除死亡玩家
            for(int i=0;i<playheath.size();i++){
                h = playheath.get(getKeyAtIndex(playheath, i));
                if(h<0){
                    playheath.remove(getKeyAtIndex(playheath, i));
                }
            }

            int size =reward/ playheath.size();

            for(int i=0;i<playheath.size();i++)
            {
                String userisid = getKeyAtIndex(playheath, i);
                QueryWrapper<UserBean> userBeanQueryWrapper=new QueryWrapper<>();
                userBeanQueryWrapper.eq("userid",userisid);
                UserBean userBean = userMapper.selectOne(userBeanQueryWrapper);
                userBean.setPoints(userBean.getPoints()+size);
                userMapper.update(new UserBean(),userBeanQueryWrapper);
            }

            //刷新冷却时间
            monsterBean.setSuccessDate(new Date());
            QueryWrapper<MonsterBean> monsterBeanQueryWrapper=new QueryWrapper<>();
            monsterBeanQueryWrapper.eq("id",monsterBean.getId());
            monsterMapper.update(monsterBean,monsterBeanQueryWrapper);

            //设置游戏状态为false
            BaseVar.ischallengeBoss=false;
            sb.append("\n怪物寄了，掉落:"+reward+"积分!(已被存活玩家平分)");


        }
        return sb.toString();
    }

    public String bossatack(){
        int math= (int) (Math.random()*10);
        int attak;
        StringBuffer sbi=new StringBuffer();
        String keyAtIndex;
        if(math>=7){
            attak=skills.get(getKeyAtIndex(skills,1));
            keyAtIndex  = getKeyAtIndex(skills, 1);

        }else {
            attak=skills.get(getKeyAtIndex(skills,0));
             keyAtIndex = getKeyAtIndex(skills, 0);

        }
        for(int i=0;i<playheath.size();i++){
            //如果这个玩家有防御
            if(playDefense.containsKey(getKeyAtIndex(playheath,i))){
               attak= attak-(attak*(playDefense.get(getKeyAtIndex(playheath,i))/100));
            }
            playheath.put(getKeyAtIndex(playheath,i),playheath.get(getKeyAtIndex(playheath,i))-attak);
        }

        sbi.append("魔兽 使用了"+keyAtIndex+"攻击!\n对周围玩家普遍造成了"+attak+"攻击伤害");
        sbi.append("\n本回合\n怪物剩余生命:"+health+"\n");

        for(int i=0;i<playheath.size();i++){
           //过滤死亡玩家
            Integer h = playheath.get(getKeyAtIndex(playheath, i));
            if(h>=0){
                sbi.append("玩家"+i+"本回合生命值还剩下:"+h+"\n");
            }else {
                sbi.append("玩家"+i+"已倒下\n");
            }
        }
        return sbi.toString();

    }
//    public static void main(String[] args) {
//        // Create a LinkedHashMap
//        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
//
//        // Add entries in a specific order
//        linkedHashMap.put("Three", 3);
//        linkedHashMap.put("Two", 2);
//        linkedHashMap.put("One", 1);
//        linkedHashMap.put("A", 5);
//
//        linkedHashMap.put("B", 1);
//        System.out.println(linkedHashMap.get(1));
//        // Iterate over entries
//        for (Map.Entry<String, Integer> entry : linkedHashMap.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
//    }

    // 获取Map中第n个元素的key名
    private static <K, V> K getKeyAtIndex(Map<K, V> map, int index) {
        if (index < 0 || index >= map.size()) {
            return null; // 索引越界
        }

        int i = 0;
        for (K key : map.keySet()) {
            if (i == index) {
                return key;
            }
            i++;
        }

        return null; // 理论上不会执行到这里
    }

    private static <K, V> int getKeyPosition(Map<K, V> map, K key) {
        int position = 0;
        for (K currentKey : map.keySet()) {
            if (currentKey.equals(key)) {
                return position;
            }
            position++;
        }
        // 如果未找到键，则返回 -1
        return -1;
    }


    //0 攻击 1投掷 2使用
    public BufferBean usething(String userid, String thing, int usemode){

        QueryWrapper<ShopBean> shopBeanQueryWrapper=new QueryWrapper<>();
        shopBeanQueryWrapper.eq("tradname",thing);
        //背包是否有这个物品
        ShopBean shopBean = shopMapper.selectOne(shopBeanQueryWrapper);
        if(shopBean==null)
            return null;

        //消耗品的消耗
        if(usemode!=0 || shopBean.getConsume()==1) {

            int traceid = shopBean.getId();
            QueryWrapper<BagBean> bagBeanQueryWrapper=new QueryWrapper<>();
            bagBeanQueryWrapper.eq("userid",userid);
            BagBean bagBean = bagMapper.selectOne(bagBeanQueryWrapper);
            if(bagBean==null){
                return null;
            }
            for(int i=1;i<=10;i++){
                String t = shopBagService.getT(bagBean, i);

                if(t!=null && t.startsWith(thing)){
                    String[] ts = t.split("\\*");
                    int yournum=Integer.parseInt(ts[1])-1;
                    if(yournum<0){
                        shopBagService.setT(bagBean,i,null);
                        return null;
                    }
                    shopBagService.setT(bagBean,i,traceid+"*"+yournum);
                    bagMapper.update(bagBean,bagBeanQueryWrapper);
                    break;
                }
            }

        }
        //对于bufferinfo的解读
        String bufferinfo = shopBean.getBufferinfo();
        String[] bufferarr = bufferinfo.split(",");
        BufferBean bufferBean = new BufferBean();
        for (String s : bufferarr) {
            if(s.contains("攻击伤害")){
                int attack=0;
                attack+= Integer.parseInt(s.replace("攻击伤害:", ""));
               bufferBean.setAddAttack(bufferBean.getAddAttack()+attack);
               //投掷
               if(usemode==1){
                   Random random = new Random();
                   // 生成在10到30之间的随机整数
                   int randomNumber = random.nextInt(21) + 10;
                   bufferBean.setAddAttack(bufferBean.getAddAttack()+randomNumber);
               }
            }else if(s.contains("回复血量")){

                    int temp=0;
                    temp+= Integer.parseInt(s.replace("回复血量:", ""));
                    bufferBean.setReturnHeath(bufferBean.getReturnHeath()+temp);

            }
            else if(s.contains("增加血量")){

                    int temp=0;
                    temp+= Integer.parseInt(s.replace("增加血量:", ""));
                    bufferBean.setAddHeath(bufferBean.getAddHeath()+temp);


            }
            else if(s.contains("增加防御")){
                //不算消耗品
                int temp=0;
                temp+= Integer.parseInt(s.replace("增加防御:", ""));
                bufferBean.setAddDefense(bufferBean.getAddDefense()+temp);

            }else if(s.contains("暴击")){
                int temp = 0;
                String[] str = s.split("\\*");
                double randomValue = Math.random();


                if (randomValue < Double.parseDouble(str[1])) {
                    temp+=Integer.parseInt(str[0]);
                    bufferBean.setAddAttack(bufferBean.getAddAttack()+temp);
                }

            }
        }
        return bufferBean;


    }
    // 判断给定时间加上n分钟后是否在当前时间之前
    private static boolean isDateBeforeNMinutes(Date originalDate, int n) {
        // 创建一个Calendar对象，并设置为原始时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalDate);

        // 将Calendar对象的时间增加n分钟
        calendar.add(Calendar.MINUTE, n);

        // 获取增加后的时间
        Date newDate = calendar.getTime();

        // 判断newDate是否在当前时间之前
        return newDate.before(new Date());
    }

}

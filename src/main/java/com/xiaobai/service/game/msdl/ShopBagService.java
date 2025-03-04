package com.xiaobai.service.game.msdl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaobai.mapping.BagMapper;
import com.xiaobai.mapping.ShopMapper;
import com.xiaobai.mapping.UserMapper;
import com.xiaobai.pojo.entity.BagBean;
import com.xiaobai.pojo.entity.ShopBean;
import com.xiaobai.pojo.entity.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
public class ShopBagService {

    @Autowired
    BagMapper bagMapper;

    @Autowired
    ShopMapper shopMapper;

    @Autowired
    UserMapper userMapper;

    public String getBag(String userid){
        int sum=0;
        StringBuffer sb=new StringBuffer();
        QueryWrapper<BagBean> wrapper=new QueryWrapper<>();
        wrapper.eq("userid",userid);
        BagBean bagBean = bagMapper.selectOne(wrapper);
        if(bagBean==null){
            return "背包为空!";
        }
        else if(bagBean.getCount()==0){
            return "背包为空";
        }
        else{
            for(int i=0;i<=10;i++){
                String thing = getT(bagBean, i);
                if(thing!=null && !thing.equals("null")){
                    if(thing.contains("null"))continue;
                    sb.append((++sum)+":"+thing+"\n");
                }
            }
            if(sb.toString().equals("")){
                return "背包为空!";
            }
            return sb.toString();

        }

    }

    public String getShop(){
        StringBuffer sb=new StringBuffer();
        QueryWrapper<ShopBean> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("isshow",1);
        List<ShopBean> shopBeans = shopMapper.selectList(queryWrapper);
        for (ShopBean shopBean : shopBeans) {
//                sb.append(shopBean.getTradname()+"(售价:"+shopBean.getPrice()+" 积分 | 库存:无限"+" ):\n介绍:"+
//                        shopBean.getInfo()+"\n附加属性:"+shopBean.getBufferinfo()+"\n\n");
            sb.append(shopBean.getTradname()+"(售价:"+shopBean.getPrice()+" 积分 | 库存:无限"+" ):\n");
        }
        sb.append("查看商品详情请输入\"查看 XXX\"(例如:查看 匕首)");
        return sb.toString();
    }


    public String buy(String userid,String thing){

        int allcount=0;
        QueryWrapper<ShopBean> wrapper = new QueryWrapper<>();
        wrapper.eq("tradname", thing);
        ShopBean shopBean = shopMapper.selectOne(wrapper);
        int traceid = shopBean.getId();
        if(shopBean!=null && shopBean.getNum()!=0){
            QueryWrapper<UserBean> queryWrapper=new QueryWrapper();
            queryWrapper.eq("userid",userid);
            UserBean userBean = userMapper.selectOne(queryWrapper);
            if(userBean!=null){
                //普通用户 0 5个槽
                switch(userBean.getPower()){
                    case 0:
                        allcount=5;
                        break;
                    case 1:
                        allcount=8;
                        break;
                    case 2:
                    case -1:
                        allcount=10;
                        break;

                }
                if(shopBean.getPrice()>userBean.getPoints()){
                    return "积分不足";
                }



                else{
                    userBean.setPoints(userBean.getPoints()-shopBean.getPrice());
                    userMapper.update(userBean,queryWrapper);

                    QueryWrapper<BagBean> bagBeanQueryWrapper=new QueryWrapper<>();
                    bagBeanQueryWrapper.eq("userid",userid);
                    BagBean bagBean = bagMapper.selectOne(bagBeanQueryWrapper);
                    if(bagBean==null){
                        bagBean=new BagBean(null,userid,traceid+"*1",null,null,null,null,null,null,null,null,null);
                        bagMapper.insert(bagBean);
                        return "购买成功";
                    }
                    //不是null
                    for(int i=1;i<=allcount;i++){
                        String t = getT(bagBean, i);

                        //如果有相同的物品
                        if(t!=null && t.startsWith(thing)){
                            String[] ts = t.split("\\*");
                            int yournum=Integer.parseInt(ts[1])+1;

                            setT(bagBean,i,traceid+"*"+yournum);
                            bagMapper.update(bagBean,bagBeanQueryWrapper);
                            return "购买成功";
                        }
                    }
                    //没有相同的物品
                    for(int i=1;i<=allcount;i++){
                        String t = getT(bagBean, i);
                        if(t==null|| t.equals("null")){
                            setT(bagBean,i,traceid+"*"+1);
                            bagMapper.update(bagBean,bagBeanQueryWrapper);
                            return "购买成功";
                        }
                    }
                    if(allcount!=10)
                        return "背包空间不足,请升级会员提升背包空间";
                    else
                        return "您的背包已满";

                }
            }else {
                return "积分不足";
            }
        }else {
            return "购买失败，商品不存在或库存不足";
        }

    }
    public String getT(BagBean bagBean, int index) {
        // 根据index获取对应的getT方法
        String methodName = "getT" + index;

        try {
            // 通过反射调用对应的getT方法
            Method method = bagBean.getClass().getMethod(methodName);
            String thingnum = (String) method.invoke(bagBean);
            String[] thingandnum = thingnum.split("\\*");
            QueryWrapper<ShopBean> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",Integer.parseInt(thingandnum[0]));
            ShopBean shopBean = shopMapper.selectOne(queryWrapper);
            if(shopBean!=null){
                return shopBean.getTradname()+"*"+thingandnum[1];
            }
        } catch (Exception e) {
            // 处理异常，例如方法不存在等情况
            return null;
        }
        return null;
    }
    public void setT(BagBean bagBean, int index, String value) {
        // 根据index获取对应的setT方法
        String methodName = "setT" + index;

        try {
            // 获取对应的setT方法
            Method method = bagBean.getClass().getMethod(methodName, String.class);
            // 通过反射调用对应的setT方法
            method.invoke(bagBean, value);
        } catch (Exception e) {
            // 处理异常，例如方法不存在等情况
            e.printStackTrace();  // 这里可以根据实际需求进行异常处理
        }
    }

    public static void main(String[] args) {
        String abc="刀*4";
        System.out.println(abc.split("\\*")[1]);

    }


    public String see(String itemTo) {
        QueryWrapper<ShopBean> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("tradname",itemTo);
        ShopBean shopBean = shopMapper.selectOne(queryWrapper);
        if(shopBean==null)
        {
            return "抱歉，您输入的物品名有误无法查询到";
        }
        return   shopBean.getTradname()+"(售价:"+shopBean.getPrice()+" 积分 | 库存:无限"+" ):\n介绍:"+
                shopBean.getInfo()+"\n附加属性:"+shopBean.getBufferinfo();


    }
}

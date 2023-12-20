package com.xiaobai.service.game.msdl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaobai.pojo.entity.UserBean;
import com.xiaobai.mapping.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointsService {
    @Autowired
    private UserMapper userMapper;

    public boolean addPoints(String userid, double points,int power) {
        QueryWrapper<UserBean> wrapper = new QueryWrapper<>();
        wrapper.eq("userid",userid);
        UserBean userBean = userMapper.selectOne(wrapper);
        if(userBean!=null){
            userBean.setPoints(userBean.getPoints()+points);
            userMapper.update(userBean,wrapper );
            return true;
        }
        userMapper.insert(new UserBean(null, userid, 1000+points, power));
        return  true;
    }

    public double getPoints(String userid){
        QueryWrapper<UserBean> wrapper = new QueryWrapper<>();
        wrapper.eq("userid",userid);
        UserBean userBean = userMapper.selectOne(wrapper);
        if(userBean!=null){
            return userBean.getPoints();
        }
        return 1000;

    }

    public String getPower(String userid){
        QueryWrapper<UserBean> wrapper = new QueryWrapper<>();
        wrapper.eq("userid",userid);
        UserBean userBean = userMapper.selectOne(wrapper);
        StringBuffer sb=new StringBuffer();
        if(userBean!=null){
            switch (userBean.getPower()){
                case -1:
                    sb.append("您的身份是:管理员");
                    break;
                case 0:
                    sb.append("您的身份是:普通用户");
                    sb.append("\n您的积分为:"+userBean.getPoints());
                    sb.append("\n您可充值 36元升级VIP 78元升级至SVIP!");
                    break;
                case 1:
                    sb.append("您的身份是:VIP用户");
                    sb.append("\n您的积分为:"+userBean.getPoints());
                    sb.append("\n您可再充值  42元升级至SVIP!");
                    break;
                case 2:
                    sb.append("您的身份是:尊贵的SVIP");
                    sb.append("\n您的积分为:"+userBean.getPoints());
                    break;

            }

        }
        return sb.toString();
    }
}

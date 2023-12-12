package com.xiaobai.service;

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
        userMapper.insert(new UserBean(null, userid, 500+points, power));
        return  true;
    }

    public double getPoints(String userid){
        QueryWrapper<UserBean> wrapper = new QueryWrapper<>();
        wrapper.eq("userid",userid);
        UserBean userBean = userMapper.selectOne(wrapper);
        if(userBean!=null){
            return userBean.getPoints();
        }
        return 500;

    }
}

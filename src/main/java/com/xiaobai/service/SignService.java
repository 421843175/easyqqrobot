package com.xiaobai.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaobai.pojo.entity.SignBean;
import com.xiaobai.mapping.SignMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SignService {
    @Autowired
    SignMapper signMapper;

    public int toSign(String userid){

        QueryWrapper<SignBean> wrapper = new QueryWrapper<>();
        wrapper.eq("userid",userid);
        SignBean signBean = signMapper.selectOne(wrapper);
        if(signBean!=null){
            Date signedtime = signBean.getSignedtime();
            int resultday = dateIsTodayOrYesterday(signedtime);
            if(resultday==1){
                return -1;
            }
            else if(resultday==2){
                Integer signnum=signBean.getSigned()+ 1;
                signBean.setSigned(signnum);
                signBean.setSignedtime(new Date());
                signMapper.update(signBean,wrapper );
                return signnum;
            }
            else{
                //不是今天不是昨天
                signBean.setSigned(0);
                signBean.setSignedtime(new Date());
                signMapper.update(signBean,wrapper );
                return 1;
            }

        }
        Date date = new Date();
        signMapper.insert(new SignBean(null, userid, 1, date));
        return 1;
    }

    public static int dateIsTodayOrYesterday(Date date) {
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");

        String year = sdfYear.format(date);
        String month = sdfMonth.format(date);
        String day = sdfDay.format(date);

        Date currentDate = new Date();
        String currentYear = sdfYear.format(currentDate);
        String currentMonth = sdfMonth.format(currentDate);
        String currentDay = sdfDay.format(currentDate);

        if (year.equals(currentYear) && month.equals(currentMonth) && day.equals(currentDay)) {
            return 1; // 今天
        }

        // 获取昨天的日期
        currentDate.setTime(currentDate.getTime() - 24 * 60 * 60 * 1000);
        currentYear = sdfYear.format(currentDate);
        currentMonth = sdfMonth.format(currentDate);
        currentDay = sdfDay.format(currentDate);

        if (year.equals(currentYear) && month.equals(currentMonth) && day.equals(currentDay)) {
            return 2; // 昨天
        }

        return 0; // 不是今天也不是昨天
    }

}

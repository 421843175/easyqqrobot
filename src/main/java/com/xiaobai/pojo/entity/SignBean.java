package com.xiaobai.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sign")
public class SignBean {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userid;
    private Integer signed;
    private Date signedtime;

//    public static void main(String[] args) {
//        Date date = new Date();
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
//        SimpleDateFormat sdf3 = new SimpleDateFormat("dd");
//        String year = sdf1.format(date);
//        String month = sdf2.format(date);
//        String day = sdf3.format(date);
//        System.out.println("当前时间为：" + year + "年" + month + "月" + day + "日");
//    }
}

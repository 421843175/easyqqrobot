package com.xiaobai.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class UserBean {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userid;
    private double points;
    /*
    * -1  管理
    * 0  普通用户
    * 1 VIP
    * 2 SVIP
    * */
    private Integer power;



}

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
@TableName("monster")
public class MonsterBean {
    @TableId(type = IdType.AUTO)
    private int id;
    private String name;
    private String info;
    private String buffer;
    private int reward;
    private int cool;
    private Date successDate;  //success_date

}

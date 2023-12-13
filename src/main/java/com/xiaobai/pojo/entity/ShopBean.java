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
@TableName("shop")
public class ShopBean {
    @TableId(type = IdType.AUTO)
    private int id;
    private String tradname;
    private String info;
    private String bufferinfo;
    private int num;
    private double price;
}

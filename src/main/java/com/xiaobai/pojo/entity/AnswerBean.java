package com.xiaobai.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiaobai
 * @date 2023/12/12-15:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("answer")
public class AnswerBean {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String keyword;
    private String answer;
    private String game;
}

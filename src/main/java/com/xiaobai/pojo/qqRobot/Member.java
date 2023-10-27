package com.xiaobai.pojo.qqRobot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @AUTHOR XiaoYang
 * @DATE 2023/10/23
 * @DESCRIPTION TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    /**
     * 用户的频道基础信息，只有成员相关接口中会填充此信息
     */
    private User user;
    /**
     * 用户的昵称
     */
    private String nick;
    /**
     * 用户在频道内的身份组ID,
     * 身份组ID默认值 	描述
     * 1 	全体成员
     * 2 	管理员
     * 4 	群主/创建者
     * 5 	子频道管理员
     */
    private String[] roles;
    /**
     * 用户加入频道的时间
     */
    private Date joined_at;
}

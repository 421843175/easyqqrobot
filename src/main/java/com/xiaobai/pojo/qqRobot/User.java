package com.xiaobai.pojo.qqRobot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @AUTHOR XiaoYang
 * @DATE 2023/10/22
 * @DESCRIPTION TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String username;
    private String avatar;
    private boolean bot;
    private String union_openid;
    private String union_user_account;
}

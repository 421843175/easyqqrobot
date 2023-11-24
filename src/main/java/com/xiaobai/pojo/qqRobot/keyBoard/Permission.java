package com.xiaobai.pojo.qqRobot.keyBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaobai
 * @date 2023/10/30-23:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    int type;
    String[] specify_role_ids;
    String[] specify_user_ids;
}

package com.xiaobai.pojo;

import com.xiaobai.common.GameMode;
import com.xiaobai.common.RobotMode;
import lombok.Data;

/**
 * @author xiaobai
 * @date 2023/12/20-11:14
 */
@Data
public class Mode {
    public RobotMode robotMode = null;
    public GameMode gameMode = null;
    public boolean ischallengeBoss = false;

    public Mode(RobotMode robotMode) {
        this.robotMode = robotMode;
    }

    public String modeTip() {
        return "现在的模式是\t" + this.robotMode.getModeName();
    }
}

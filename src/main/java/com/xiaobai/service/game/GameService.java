package com.xiaobai.service.game;

import com.xiaobai.dto.MessageDto;
import com.xiaobai.pojo.qqRobot.Message;

/**
 * @author xiaobai
 * @date 2023/12/12-15:50
 */
public interface GameService {
    void playGame(MessageDto message);
    String buildAnswer(MessageDto message);
}

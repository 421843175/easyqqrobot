package com.xiaobai.pojo.qqRobot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @AUTHOR XiaoYang
 * @DATE 2023/10/23
 * @DESCRIPTION TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEmbed {
    /**
     * 标题
     */
    private String title;
    /**
     * 消息弹窗内容
     */
    private String prompt;
    /**
     * 缩略图
     */
    private MessageEmbedThumbnail thumbnail;
    /**
     * embed字段数据
     */
    private MessageEmbedField[] fields;
}

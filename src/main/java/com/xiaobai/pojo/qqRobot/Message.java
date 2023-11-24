package com.xiaobai.pojo.qqRobot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;

/**
 * @AUTHOR XiaoYang
 * @DATE 2023/10/23
 * @DESCRIPTION TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    /**
     * 消息id
     */
    private String id;
    /**
     * 子频道id
     */
    private String channel_id;

    /**
     * 频道id
     */
    private String guild_id;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息创建时间
     */
    private Date timestamp;
    /**
     * 消息编辑时间
     */
    private Date edited_timestamp;
    /**
     * 是否是@全员消息
     */
    private boolean mention_everyone;
    /**
     * 消息创建者
     */
    private User author;
    /**
     * 附件
     */
    private MessageAttachment[] attachments;
    /**
     * embed
     */
    private MessageEmbed[] embeds;
    /**
     * 消息中@的人
     */
    private User[] mentions;
    /**
     * 消息创建者的member信息
     */
    private Member member;
    /**
     * ark消息
     */
    private MessageArk ark;
    /**
     * 用于消息间的排序，seq 在同一子频道中按从先到后的顺序递增，不同的子频道之间消息无法排序。(目前只在消息事件中有值，2022年8月1日 后续废弃)
     */
    private int seq;
    /**
     * 子频道消息 seq，用于消息间的排序，seq 在同一子频道中按从先到后的顺序递增，不同的子频道之间消息无法排序
     */
    private String seq_in_channel;
    /**
     * 引用消息对象
     */
    private MessageReference message_reference;
    /**
     * 用于私信场景下识别真实的来源频道id
     */
    private String src_guild_id;

    //群聊
    /**
     * 群聊id
     */
    private String group_id;
    private String group_openid;

    @Override
    public String toString() {
        return "\n-----------------------------------------------------------------\n" +
                "id='" + id + '\n' +
                "channel_id='" + channel_id + '\n' +
                "guild_id='" + guild_id + '\n' +
                "content='" + content + '\n' +
                "timestamp=" + timestamp +'\n' +
                "edited_timestamp=" + edited_timestamp +'\n' +
                "mention_everyone=" + mention_everyone +'\n' +
                "author=" + author +'\n' +
                "attachments=" + Arrays.toString(attachments) +'\n' +
                "embeds=" + Arrays.toString(embeds) +'\n' +
                "mentions=" + Arrays.toString(mentions) +'\n' +
                "member=" + member +'\n' +
                "ark=" + ark +'\n' +
                "seq=" + seq +'\n' +
                "seq_in_channel='" + seq_in_channel + '\'' +'\n' +
                "message_reference=" + message_reference +'\n' +
                "src_guild_id='" + src_guild_id + '\'' +'\n' +
                '}';
    }
}

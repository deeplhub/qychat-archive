package com.xh.qychat.domain.qychat.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 会话消息内容
 *
 * @TableName qychat_message_content
 */
@Data
@TableName(value = "qychat_message_content")
public class MessageContentEntity {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 消息记录值,Uint64类型
     */
    private Long seq;

    /**
     * 消息id，消息的唯一标识
     */
    private String msgid;

    /**
     * 消息动作，send-发送消息,recall-撤回消息,switch-切换企业日志
     */
    private String action;

    /**
     * 消息发送方id。同一企业内容为userid，非相同企业为external_userid。消息如果是机器人发出，也为external_userid
     */
    private String fromid;

    /**
     * 消息接收方列表，可能是多个，同一个企业内容为userid，非相同企业为external_userid。数组
     */
    private String tolist;

    /**
     * 群聊消息的群id。如果是单聊则为空
     */
    private String roomid;

    /**
     * 消息发送时间戳，utc时间，ms单位
     */
    private Date msgtime;

    /**
     * 文本消息为：text/image/file
     */
    private String msgtype;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
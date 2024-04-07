package com.xh.qychat.domain.qychat.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息记录值,Uint64类型
     */
    private Long seq;

    /**
     * 消息id，消息的唯一标识
     */
    private String msgid;

    /**
     * 公钥版本号
     */
    private String publickeyVer;

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
     * 消息类型
     */
    private String msgtype;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 原始消息内容
     */
    //private String originalContent;

    /**
     * 媒体状态：1.未加载媒体，2.正在加载媒体，3.媒体加载完成，4.媒体加载失败
     * 媒体状态：1未加载，2加载成功，3.加载失败
     */
    private Integer mediaStatus;

    /**
     * 创建时间
     */
    private Date createTime;
}
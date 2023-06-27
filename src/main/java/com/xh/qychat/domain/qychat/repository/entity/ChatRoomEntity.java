package com.xh.qychat.domain.qychat.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 微信群详情
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Data
@TableName("qychat_chat_room")
public class ChatRoomEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群ID
     */
    private String chatId;

    /**
     * 群名称
     */
    private String name;

    /**
     * 群公告
     */
    private String notice;

    /**
     * 群主ID
     */
    private String owner;

    /**
     * 群信息md5签名，用于判断数据是否一致，保存或更新需要更新签名信息。sign=群ID+群名称+群公告+群主ID
     */
    private String sign;

    /**
     * 群创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}

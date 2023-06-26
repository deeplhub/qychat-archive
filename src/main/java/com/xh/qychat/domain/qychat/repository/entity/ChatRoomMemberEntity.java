package com.xh.qychat.domain.qychat.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 微信群与用户关系
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Data
@TableName("qychat_chat_room_member")
public class ChatRoomMemberEntity {

    /**
     * 企业微信客户群ID
     */
    private String chatId;

    /**
     * 成员ID
     */
    private String userId;

}

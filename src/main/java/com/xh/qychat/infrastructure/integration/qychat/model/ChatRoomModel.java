package com.xh.qychat.infrastructure.integration.qychat.model;

import lombok.Data;

import java.util.List;

/**
 * 客户群详情
 *
 * @author H.Yang
 * @date 2022/1/19
 */
@Data
public class ChatRoomModel extends ResponseModel {

    /**
     * 客户群ID
     */
    private String chat_id;

    /**
     * 群名
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
     * 群的创建时间
     */
    private Long create_time;

    /**
     * 获取客户群详情
     */
    private ChatRoomModel group_chat;

    /**
     * 群成员列表
     */
    private List<ChatRoomModel> member_list;
}

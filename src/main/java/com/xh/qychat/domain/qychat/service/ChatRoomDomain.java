package com.xh.qychat.domain.qychat.service;

import com.xh.qychat.domain.qychat.model.ChatRoom;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
public interface ChatRoomDomain {

    boolean saveOrUpdate(ChatRoom chatRoom);

    boolean saveOrUpdateBatch(List<ChatRoom> chatRooms);

    /**
     * 查询所有群列表
     *
     * @return
     */
    List<ChatRoom> listChatRoom();
}

package com.xh.qychat.domain.qychat.service;

import com.xh.qychat.domain.qychat.model.ChatRoom;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
public interface ChatRoomDomain {

    boolean saveOrUpdateBatch(List<ChatRoom> chatRooms);
}

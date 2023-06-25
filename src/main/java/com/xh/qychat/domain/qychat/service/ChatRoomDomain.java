package com.xh.qychat.domain.qychat.service;

import com.xh.qychat.domain.qychat.model.ChatRoom;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
public interface ChatRoomDomain {

    boolean saveOrUpdateBatch(ChatRoom chatRoom);
}

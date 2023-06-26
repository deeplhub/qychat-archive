package com.xh.qychat.domain.qychat.model;

import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
@Data
@NoArgsConstructor
public class ChatRoom {

    private Set<ChatRoomModel> chatRoomModelList;

    public ChatRoom(Set<ChatRoomModel> chatRoomModelList) {
        this.chatRoomModelList = chatRoomModelList;
    }
}

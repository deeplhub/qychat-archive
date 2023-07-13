package com.xh.qychat.domain.qychat.model.factory;

import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
public class ChatRoomFactory {

    private static class Inner {
        private static final ChatRoomFactory instance = new ChatRoomFactory();
    }

    private ChatRoomFactory() {
    }

    public static ChatRoomFactory getSingleton() {
        return ChatRoomFactory.Inner.instance;
    }


    public List<ChatRoomEntity> createOrModifyEntity(List<ChatRoom> chatRooms, List<ChatRoomEntity> chatRoomList) {
        Map<String, ChatRoomEntity> dictMap = chatRoomList.parallelStream().collect(HashMap::new, (k, v) -> k.put(v.getChatId(), v), HashMap::putAll);

        return chatRooms.parallelStream().map(o -> this.getChatRoomEntity(dictMap, o)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public ChatRoomEntity createOrModifyEntity(ChatRoom chatRoom, ChatRoomEntity chatRoomEntity) {

        return this.getChatRoomEntity(chatRoom, chatRoomEntity);
    }

    private ChatRoomEntity getChatRoomEntity(Map<String, ChatRoomEntity> dictMap, ChatRoom chatRoom) {

        return this.getChatRoomEntity(chatRoom, dictMap.get(chatRoom.getChatId()));
    }

    private ChatRoomEntity getChatRoomEntity(ChatRoom chatRoom, ChatRoomEntity chatRoomEntity) {
        chatRoomEntity = (chatRoomEntity == null) ? new ChatRoomEntity() : chatRoomEntity;

        if ((chatRoom.getSign() + "").equals(chatRoomEntity.getSign())) return null;


        return this.toChatRoomEntity(chatRoom, chatRoomEntity);
    }

    private ChatRoomEntity toChatRoomEntity(ChatRoom chatRoom, ChatRoomEntity chatRoomEntity) {
        chatRoomEntity.setChatId(chatRoom.getChatId());
        chatRoomEntity.setName(chatRoom.getName());
        chatRoomEntity.setNotice(chatRoom.getNotice());
        chatRoomEntity.setOwner(chatRoom.getOwner());
        chatRoomEntity.setCreateTime(chatRoom.getCreateTime());

        return chatRoomEntity;
    }

    public ChatRoom toChatRoom(ChatRoomEntity chatRoomEntity) {
        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setChatId(chatRoomEntity.getChatId());
        chatRoom.setName(chatRoomEntity.getName());
        chatRoom.setNotice(chatRoomEntity.getNotice());
        chatRoom.setOwner(chatRoomEntity.getOwner());
        chatRoom.setCreateTime(chatRoomEntity.getCreateTime());

        return chatRoom;
    }
}

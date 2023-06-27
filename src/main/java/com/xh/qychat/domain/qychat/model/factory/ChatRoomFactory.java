package com.xh.qychat.domain.qychat.model.factory;

import cn.hutool.core.date.DateUtil;
import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;

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


    public List<ChatRoomEntity> createOrModifyEntity(ChatRoom chatRoom, List<ChatRoomEntity> chatRoomList) {
        Map<String, ChatRoomEntity> dictMap = chatRoomList.parallelStream().collect(HashMap::new, (k, v) -> k.put(v.getChatId(), v), HashMap::putAll);

        Set<ChatRoomModel> chatRoomModelList = chatRoom.getChatRoomModelList();

        return chatRoomModelList.parallelStream().map(o -> getChatRoomEntity(dictMap, o)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private ChatRoomEntity getChatRoomEntity(Map<String, ChatRoomEntity> dictMap, ChatRoomModel chatRoomModel) {
        ChatRoomEntity chatRoomEntity = dictMap.get(chatRoomModel.getChatId());
        chatRoomEntity = (chatRoomEntity == null) ? new ChatRoomEntity() : chatRoomEntity;

        if ((chatRoomModel.getSign() + "").equals(chatRoomEntity.getSign())) return null;

        chatRoomEntity.setChatId(chatRoomModel.getChatId());
        chatRoomEntity.setName(chatRoomModel.getName());
        chatRoomEntity.setNotice(chatRoomModel.getNotice());
        chatRoomEntity.setOwner(chatRoomModel.getOwner());
        chatRoomEntity.setCreateTime(DateUtil.date(chatRoomModel.getCreateTime()));

        return chatRoomEntity;
    }

    public Set<String> listChatId(ChatRoom chatRoom) {
        return chatRoom.getChatRoomModelList().parallelStream().filter(Objects::nonNull).map(o -> o.getChatId()).collect(Collectors.toSet());

    }

}

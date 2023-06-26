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

    public static List<ChatRoomEntity> createOrModifyEntity(ChatRoom chatRoom, List<ChatRoomEntity> chatRoomList) {
        Map<String, ChatRoomEntity> dictMap = chatRoomList.parallelStream().collect(HashMap::new, (k, v) -> k.put(v.getChatId(), v), HashMap::putAll);

        Set<ChatRoomModel> chatRoomModelList = chatRoom.getChatRoomModelList();
        List<ChatRoomEntity> entityList = new LinkedList<>();

        chatRoomModelList.parallelStream().filter(Objects::nonNull).forEach(item -> {
            ChatRoomEntity chatRoomEntity = dictMap.get(item.getChatId());
            chatRoomEntity = (chatRoomEntity == null) ? new ChatRoomEntity() : chatRoomEntity;

            String sign = item.getSign() + "";
            System.out.println(sign + "  ===  " + chatRoomEntity.getSign());
            if (sign.equals(chatRoomEntity.getSign())) return;

            chatRoomEntity.setChatId(item.getChatId());
            chatRoomEntity.setName(item.getName());
            chatRoomEntity.setNotice(item.getNotice());
            chatRoomEntity.setOwner(item.getOwner());
            chatRoomEntity.setCreateTime(DateUtil.date(item.getCreateTime()));

            entityList.add(chatRoomEntity);
        });

        return entityList;
    }

    public static Set<String> listChatId(ChatRoom chatRoom) {
        return chatRoom.getChatRoomModelList().parallelStream().filter(Objects::nonNull).map(o -> o.getChatId()).collect(Collectors.toSet());

    }

}

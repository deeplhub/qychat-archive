package com.xh.qychat.domain.qychat.service.impl;

import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.model.factory.ChatRoomFactory;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomEntity;
import com.xh.qychat.domain.qychat.repository.service.ChatRoomService;
import com.xh.qychat.domain.qychat.service.ChatRoomDomain;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import com.xh.qychat.infrastructure.redis.JedisRepository;
import com.xh.qychat.infrastructure.util.KeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
@Slf4j
@Service
public class ChatRoomDomainImpl implements ChatRoomDomain {

    @Resource
    private ChatRoomService chatRoomService;
    @Resource
    private JedisRepository jedisRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(ChatRoom chatRoom) {
        ChatRoomEntity entity = chatRoomService.getByChatId(chatRoom.getChatId());
        entity = ChatRoomFactory.getSingleton().createOrModifyEntity(chatRoom, entity);
        if (entity == null) {
            return true;
        }

        return chatRoomService.saveOrUpdate(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(List<ChatRoom> chatRooms) {
        if (chatRooms.isEmpty()) {
            return false;
        }

        List<ChatRoom> chatRoomsTemp = new ArrayList<>();
        String cacheKey = KeyUtils.get(ChatRoomEntity.class, "ChatRoomSign");
        Set<String> chatIds = chatRooms.parallelStream().filter(Objects::nonNull).filter(o -> {
            String cacheSign = jedisRepository.hget(cacheKey, o.getChatId());
            if (o.getSign().equals(cacheSign)) {
                return false;
            }
            chatRoomsTemp.add(o);
            return true;
        }).map(o -> o.getChatId()).collect(Collectors.toSet());

        if (chatIds.isEmpty()) {
            return false;
        }

        List<ChatRoomEntity> chatRoomList = chatRoomService.listByChatId(chatIds);
        List<ChatRoomEntity> entityList = ChatRoomFactory.getSingleton().createOrModifyEntity(chatRoomsTemp, chatRoomList);
        if (entityList.isEmpty()) {
            return false;
        }

        boolean isSuccess = chatRoomService.saveOrUpdateBatch(entityList, CommonConstants.BATCH_SIZE);
        if (isSuccess) {
            entityList.parallelStream().forEach(item -> jedisRepository.hset(cacheKey, item.getChatId(), item.getSign()));
        }
        return isSuccess;
    }

    @Override
    public List<ChatRoom> listChatRoom() {
        List<ChatRoomEntity> listAll = chatRoomService.listAll();

        return listAll.parallelStream().map(o -> ChatRoomFactory.getSingleton().toChatRoom(o)).collect(Collectors.toList());
    }
}

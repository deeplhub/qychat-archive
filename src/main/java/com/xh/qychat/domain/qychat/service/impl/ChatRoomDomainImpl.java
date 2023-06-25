package com.xh.qychat.domain.qychat.service.impl;

import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.model.factory.ChatRoomFactory;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomEntity;
import com.xh.qychat.domain.qychat.repository.service.impl.ChatRoomServiceImpl;
import com.xh.qychat.domain.qychat.service.ChatRoomDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
@Slf4j
@Service
public class ChatRoomDomainImpl extends ChatRoomServiceImpl implements ChatRoomDomain {

    @Override
    @Transactional
    public boolean saveOrUpdateBatch(ChatRoom chatRoom) {
        Set<String> chatIds = ChatRoomFactory.listChatId(chatRoom);

        List<ChatRoomEntity> chatRoomList = super.listByChatId(chatIds);
        List<ChatRoomEntity> entityList = ChatRoomFactory.createOrModifyEntity(chatRoom, chatRoomList);
        if (entityList.isEmpty()) {
            log.warn("保存群信息出现错误，数据为空");
            return true;
        }

        return super.saveOrUpdateBatch(entityList, 1000);
    }
}

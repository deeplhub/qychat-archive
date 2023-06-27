package com.xh.qychat.domain.qychat.service.impl;

import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.model.factory.ChatRoomFactory;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomEntity;
import com.xh.qychat.domain.qychat.repository.service.impl.ChatRoomServiceImpl;
import com.xh.qychat.domain.qychat.service.ChatRoomDomain;
import com.xh.qychat.infrastructure.constants.CommonConstants;
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
        Set<String> chatIds = ChatRoomFactory.getSingleton().listChatId(chatRoom);

        List<ChatRoomEntity> chatRoomList = super.listByChatId(chatIds);
        List<ChatRoomEntity> entityList = ChatRoomFactory.getSingleton().createOrModifyEntity(chatRoom, chatRoomList);
        if (entityList.isEmpty()) {
            log.warn("批量保存/更新群信息时数据为空");
            return true;
        }
        return super.saveOrUpdateBatch(entityList, CommonConstants.BATCH_SIZE);
    }
}

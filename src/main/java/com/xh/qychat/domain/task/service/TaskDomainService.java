package com.xh.qychat.domain.task.service;

import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;

import java.util.List;
import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface TaskDomainService {

    /**
     * 获取会话存档数据
     *
     * @param seq 消息记录值
     * @return
     */
    List<ChatDataModel> pullChatData(Long seq);

    List<ChatRoomModel> listChatRoomDetail(Set<String> roomids);
}

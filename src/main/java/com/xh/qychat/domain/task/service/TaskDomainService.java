package com.xh.qychat.domain.task.service;

import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.integration.qychat.model.PersonnelModel;

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

    /**
     * 获取群详情
     *
     * @param roomids 群ID列表
     * @return
     */
    Set<ChatRoomModel> listChatRoomDetail(Set<String> roomids);

    /**
     * 获取群详情
     *
     * @param chatId 群ID
     * @return
     */
    ChatRoomModel getChatRoomDetail(String chatId);

    PersonnelModel getPersonnel(String userId);
}

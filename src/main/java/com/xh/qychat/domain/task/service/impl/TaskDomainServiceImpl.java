package com.xh.qychat.domain.task.service.impl;

import com.xh.qychat.domain.task.event.ChatRoomEvent;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.common.enums.ResponseEnum;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.integration.qychat.model.PersonnelModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Service
public class TaskDomainServiceImpl implements TaskDomainService {

    @Resource
    private QyChatAdapter qychatAdapter;

    @Override
    public List<ChatDataModel> pullChatData(Long seq) {

        return qychatAdapter.listChatData(seq);
    }

    @Override
    public Set<ChatRoomModel> listChatRoomDetail(Set<String> roomids) {

        return ChatRoomEvent.getTaskExecutor().setChatAdapter(qychatAdapter).listChatRoomDetail(roomids);
    }

    @Override
    public ChatRoomModel getChatRoomDetail(String roomid) {
        ChatRoomModel chatRoomDetail = qychatAdapter.getChatRoomDetail(roomid);
        if (chatRoomDetail == null) throw new RuntimeException(ResponseEnum.REQUEST_PARAMETERS.getNote());

        return chatRoomDetail;
    }

    @Override
    public PersonnelModel getPersonnel(String userId) {
        PersonnelModel personnel = qychatAdapter.getPersonnelDetail(userId);
        if (personnel == null) throw new RuntimeException(ResponseEnum.REQUEST_PARAMETERS.getNote());

        return personnel;
    }
}

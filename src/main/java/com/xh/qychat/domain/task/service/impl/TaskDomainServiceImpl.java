package com.xh.qychat.domain.task.service.impl;

import com.xh.qychat.domain.task.event.ChatRoomEvent;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Slf4j
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
}

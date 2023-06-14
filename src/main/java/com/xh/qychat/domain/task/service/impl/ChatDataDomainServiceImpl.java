package com.xh.qychat.domain.task.service.impl;

import com.xh.qychat.domain.task.service.ChatDataDomainService;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Slf4j
@Service
public class ChatDataDomainServiceImpl implements ChatDataDomainService {

    @Resource
    private QyChatAdapter qychatAdapter;

    @Override
    public List<ChatDataModel> pullChatData(Long seq) {

        return qychatAdapter.listChatData(seq);
    }
}

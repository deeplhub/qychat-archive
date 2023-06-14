package com.xh.qychat.domain.task.service;

import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface ChatDataDomainService {


    List<ChatDataModel> pullChatData(Long seq);
}

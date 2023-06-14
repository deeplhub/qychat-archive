package com.xh.qychat.application.impl;

import com.xh.qychat.application.TaskApplication;
import com.xh.qychat.domain.qychat.service.MessageContentDomainService;
import com.xh.qychat.domain.task.service.ChatDataDomainService;
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
public class TaskApplicationImpl implements TaskApplication {

    @Resource
    private ChatDataDomainService chatDataDomainService;
    @Resource
    private MessageContentDomainService messageContentDomainService;


    @Override
    public boolean pullChatData() {
        Long maxSeq = messageContentDomainService.getMaxSeq();
        List<ChatDataModel> dataModelList = chatDataDomainService.pullChatData(maxSeq);

        return messageContentDomainService.saveBath(dataModelList);
    }
}

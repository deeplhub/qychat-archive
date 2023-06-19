package com.xh.qychat.application.service.impl;

import com.xh.qychat.application.event.ResponseEvent;
import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.domain.qychat.service.MessageContentDomainService;
import com.xh.qychat.domain.task.service.ChatDataDomainService;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.common.model.Result;
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
    public Result pullChatData() {
        Long maxSeq = messageContentDomainService.getMaxSeq();
        List<ChatDataModel> dataModelList = chatDataDomainService.pullChatData(maxSeq);

        boolean isSuccess = messageContentDomainService.saveBath(dataModelList);

        return ResponseEvent.reply(isSuccess);
    }
}

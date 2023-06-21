package com.xh.qychat.application.service.impl;

import cn.hutool.core.util.StrUtil;
import com.xh.qychat.application.event.ResponseEvent;
import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.service.MessageContentDomainService;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.common.model.Result;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Slf4j
@Service
public class TaskApplicationImpl implements TaskApplication {

    @Resource
    private TaskDomainService taskDomainService;
    @Resource
    private MessageContentDomainService messageContentDomainService;


    @Override
    public Result pullChatData() {
        Long maxSeq = messageContentDomainService.getMaxSeq();
        List<ChatDataModel> dataModelList = taskDomainService.pullChatData(maxSeq);

        boolean isSuccess = messageContentDomainService.saveBath(new MessageContent(dataModelList));

        Set<String> roomIds = dataModelList.parallelStream().filter(o -> StrUtil.isNotBlank(o.getRoomid())).map(o -> o.getRoomid()).collect(Collectors.toSet());

        return ResponseEvent.reply(isSuccess);
    }

}

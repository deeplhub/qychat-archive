package com.xh.qychat.application.service.impl;

import com.xh.qychat.application.event.ResponseEvent;
import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.service.ChatRoomDomain;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.common.model.Result;
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
public class TaskApplicationImpl implements TaskApplication {

    @Resource
    private TaskDomainService taskDomainService;
    @Resource
    private MessageContentDomain messageContentDomain;
    @Resource
    private ChatRoomDomain chatRoomDomain;


    @Override
    public Result pullChatData() {
        Long maxSeq = messageContentDomain.getMaxSeq();
        List<ChatDataModel> dataModelList = taskDomainService.pullChatData(maxSeq);

        boolean isSuccess = messageContentDomain.saveBath(new MessageContent(dataModelList));

//        Set<String> roomIds = dataModelList.parallelStream().filter(o -> StrUtil.isNotBlank(o.getRoomid())).map(o -> o.getRoomid()).collect(Collectors.toSet());
        return ResponseEvent.reply(isSuccess);
    }

    @Override
    public Result pullChatRoom() {
        Set<String> roomIds = messageContentDomain.listRoomIdGoupByRoomId();

        List<ChatRoomModel> list = taskDomainService.listChatRoomDetail(roomIds);

        boolean isSuccess = chatRoomDomain.saveOrUpdateBatch(new ChatRoom(list));
        return ResponseEvent.reply(isSuccess);
    }

}

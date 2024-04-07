package com.xh.qychat.application.service.impl;

import com.xh.qychat.application.event.ResponseEvent;
import com.xh.qychat.application.service.ChatRoomApplication;
import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.model.ChatRoomTreeNode;
import com.xh.qychat.domain.qychat.service.ChatRoomDomain;
import com.xh.qychat.domain.qychat.service.MemberDomain;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.common.model.Result;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author H.Yang
 * @date 2023/7/12
 */
@Service
public class ChatRoomApplicationImpl implements ChatRoomApplication {

    @Resource
    private ChatRoomDomain chatRoomDomain;
    @Resource
    private MemberDomain memberDomain;
    @Resource
    private TaskDomainService taskDomainService;

    @Override
    public Result pullChatRoom(String chatId) {
        ChatRoomModel chatRoom = taskDomainService.getChatRoomDetail(chatId);

        boolean isSuccess = chatRoomDomain.saveOrUpdate(ChatRoom.create(chatRoom));

        // TODO 后期建议使用MQ异步调用
        if (isSuccess) {
            memberDomain.saveOrUpdateBatch(ChatRoomTreeNode.createTreeNode(chatRoom));
        }
        return ResponseEvent.reply(isSuccess);
    }


    @Override
    public Result listChatRoom() {
        List<ChatRoom> list = chatRoomDomain.listChatRoom();
        return Result.succeed(list);
    }

}

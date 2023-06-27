package com.xh.qychat.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.qychat.application.event.ResponseEvent;
import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.model.ChatRoomTreeNode;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.service.ChatRoomDomain;
import com.xh.qychat.domain.qychat.service.MemberDomain;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.common.enums.ResponseEnum;
import com.xh.qychat.infrastructure.common.model.Result;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Service
public class TaskApplicationImpl implements TaskApplication {

    @Resource
    private TaskDomainService taskDomainService;
    @Resource
    private MessageContentDomain messageContentDomain;
    @Resource
    private ChatRoomDomain chatRoomDomain;
    @Resource
    private MemberDomain memberDomain;


    @Override
    public Result pullChatData() {
        Long maxSeq = messageContentDomain.getMaxSeq();
        List<ChatDataModel> dataModelList = taskDomainService.pullChatData(maxSeq);

        boolean isSuccess = messageContentDomain.saveBath(new MessageContent(dataModelList));
        return ResponseEvent.reply(isSuccess);
    }

    @Override
    public Result pullChatRoom() {
        boolean isSuccess = this.listChatRoom(1, 100);
        return ResponseEvent.reply(isSuccess);
    }

    @Override
    public Result pullChatRoom(String roomId) {
        ChatRoomModel chatRoom = taskDomainService.getChatRoomDetail(roomId);
        if (chatRoom == null) ResponseEvent.failed(ResponseEnum.REQUEST_PARAMETERS);

        boolean isSuccess = chatRoomDomain.saveOrUpdate(ChatRoom.create(chatRoom));
        if (!isSuccess) throw new RuntimeException("save chat room fail");

        // TODO 后期建议使用MQ异步调用
        isSuccess = memberDomain.saveOrUpdateBatch(ChatRoomTreeNode.createTreeNode(chatRoom));
        return ResponseEvent.reply(isSuccess);
    }

    @Override
    public Result pullPersonnel(String userId) {
        return null;
    }

    public boolean listChatRoom(Integer pageNum, Integer limit) {
        Page<String> page = messageContentDomain.pageListRoomIdGoupByRoomId(pageNum, limit);

        List<String> records = page.getRecords();
        if (!records.isEmpty()) {
            Set<ChatRoomModel> list = taskDomainService.listChatRoomDetail(new HashSet<>(records));
            if (list.isEmpty()) return this.listChatRoom(pageNum + 1, limit);

            this.saveOrUpdateChatRoom(list);

            // TODO 后期建议使用MQ异步调用
            this.saveOrUpdateMember(list);

            this.listChatRoom(pageNum + 1, limit);
        }

        return true;
    }

    private void saveOrUpdateChatRoom(Set<ChatRoomModel> list) {
        boolean isSuccess = chatRoomDomain.saveOrUpdateBatch(ChatRoom.create(list));
        if (!isSuccess) throw new RuntimeException("save chat room fail");
    }

    private void saveOrUpdateMember(Set<ChatRoomModel> list) {
        memberDomain.saveOrUpdateBatch(ChatRoomTreeNode.createTreeNode(list));
    }
}

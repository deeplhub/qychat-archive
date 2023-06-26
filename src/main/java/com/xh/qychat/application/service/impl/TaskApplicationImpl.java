package com.xh.qychat.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.qychat.application.event.ResponseEvent;
import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.model.ChatRoomTreeNodeModel;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.service.ChatRoomDomain;
import com.xh.qychat.domain.qychat.service.MemberDomain;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.common.model.Result;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
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
        boolean isSuccess = this.recursionRoomId(1, 10);
        return ResponseEvent.reply(isSuccess);
    }

    public boolean recursionRoomId(Integer pageNum, Integer limit) {
        Page<String> page = messageContentDomain.pageListRoomIdGoupByRoomId(pageNum, limit);

        List<String> records = page.getRecords();
        if (!records.isEmpty()) {
            Set<ChatRoomModel> list = taskDomainService.listChatRoomDetail(new HashSet<>(records));
            if (list.size() == 0) return this.recursionRoomId(pageNum + 1, limit);

            boolean isSuccess = chatRoomDomain.saveOrUpdateBatch(new ChatRoom(list));
            if (!isSuccess) throw new RuntimeException("save chat room fail");

            // TODO 后期需要改成异步调用
            memberDomain.saveOrUpdateBatch(new Member(list));


            this.recursionRoomId(pageNum + 1, limit);
        }

        return true;
    }

    private void saveOrUpdateMember(Set<ChatRoomModel> list) {
        ChatRoomTreeNodeModel chatRoomTreeNodeModel = new ChatRoomTreeNodeModel();

        memberDomain.saveOrUpdateBatch2(chatRoomTreeNodeModel.createTreeNode(list));
    }
}

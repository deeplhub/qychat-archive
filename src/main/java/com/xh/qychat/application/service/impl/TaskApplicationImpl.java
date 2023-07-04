package com.xh.qychat.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.qychat.application.event.ResponseEvent;
import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.model.ChatRoomTreeNode;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.service.ChatRoomDomain;
import com.xh.qychat.domain.qychat.service.MemberDomain;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.common.enums.ResponseEnum;
import com.xh.qychat.infrastructure.common.model.Result;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.integration.qychat.model.PersonnelModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        List<ChatDataModel> dataModels = taskDomainService.pullChatData(maxSeq);

        boolean isSuccess = messageContentDomain.saveBath(MessageContent.create(dataModels));
        // TODO 后期建议使用MQ异步调用
        isSuccess = this.pullChatRoom(isSuccess, dataModels);

        return ResponseEvent.reply(isSuccess, ResponseEnum.REQUEST_PARAMETERS);
    }

    private boolean pullChatRoom(boolean isSuccess, List<ChatDataModel> dataModels) {
        if (!isSuccess) return false;

        Set<String> roomIds = dataModels.parallelStream().filter(Objects::nonNull).map(o -> o.getRoomid()).collect(Collectors.toSet());
        Set<ChatRoomModel> chatRooms = taskDomainService.listChatRoomDetail(roomIds);

        isSuccess = this.saveOrUpdateChatRoom(chatRooms);
        if (isSuccess) this.saveOrUpdateMember(chatRooms);

        return isSuccess;
    }

    @Override
    @Transactional
    public Result pullChatRoom() {
        boolean isSuccess = this.listChatRoom(1, 100);
        return ResponseEvent.reply(isSuccess);
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

    private boolean saveOrUpdateChatRoom(Set<ChatRoomModel> list) {

        return chatRoomDomain.saveOrUpdateBatch(ChatRoom.create(list));
    }

    private void saveOrUpdateMember(Set<ChatRoomModel> list) {

        memberDomain.saveOrUpdateBatch(ChatRoomTreeNode.createTreeNode(list));
    }

    @Override
    public Result pullChatRoom(String roomId) {
        ChatRoomModel chatRoom = taskDomainService.getChatRoomDetail(roomId);

        boolean isSuccess = chatRoomDomain.saveOrUpdate(ChatRoom.create(chatRoom));

        // TODO 后期建议使用MQ异步调用
        if (isSuccess) memberDomain.saveOrUpdateBatch(ChatRoomTreeNode.createTreeNode(chatRoom));
        return ResponseEvent.reply(isSuccess);
    }

    @Override
    public Result pullPersonnel(String userId) {
        PersonnelModel personnel = taskDomainService.getPersonnel(userId);

        boolean isSuccess = memberDomain.saveOrUpdate(Member.create(personnel));
        return ResponseEvent.reply(isSuccess);
    }

}

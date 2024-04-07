package com.xh.qychat.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.qychat.application.event.ResponseEvent;
import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.model.ChatRoomTreeNode;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.service.ChatRoomDomain;
import com.xh.qychat.domain.qychat.service.MemberDomain;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.common.enums.ResponseEnum;
import com.xh.qychat.infrastructure.common.model.Result;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
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
    @Transactional(rollbackFor = Exception.class)
    public Result pullChatData() {
        Long maxSeq = messageContentDomain.getMaxSeq();
        List<ChatDataModel> dataModels = taskDomainService.pullChatData(maxSeq);

        boolean isSuccess = messageContentDomain.saveBath(MessageContent.create(dataModels));
        if (!isSuccess) {
            return ResponseEvent.failed(ResponseEnum.REQUEST_PARAMETERS);
        }

        isSuccess = this.pullChatRoom(dataModels);

        return ResponseEvent.reply(isSuccess, ResponseEnum.REQUEST_PARAMETERS);
    }

    /**
     * 拉取群详情和群用户列表
     *
     * @param dataModels
     * @return
     */
    private boolean pullChatRoom(List<ChatDataModel> dataModels) {
        Set<String> roomIds = dataModels.parallelStream().filter(Objects::nonNull).map(o -> o.getRoomid()).collect(Collectors.toSet());
        Set<ChatRoomModel> chatRooms = taskDomainService.listChatRoomDetail(roomIds);

        this.saveOrUpdateChatRoom(chatRooms);
        this.saveOrUpdateMember(chatRooms);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result pullChatRoom() {
        boolean isSuccess = this.listChatRoom(1, 100);
        return ResponseEvent.reply(isSuccess);
    }

    public boolean listChatRoom(Integer pageNum, Integer limit) {
        Page<String> page = messageContentDomain.pageListRoomIdGoupByRoomId(pageNum, limit);

        List<String> records = page.getRecords();
//        if (!records.isEmpty()) {
//            Set<ChatRoomModel> list = taskDomainService.listChatRoomDetail(new HashSet<>(records));
//            if (list.isEmpty()) return this.listChatRoom(pageNum + 1, limit);
//
//            this.saveOrUpdateChatRoom(list);
//
//            // TODO 后期建议使用MQ异步调用
//            this.saveOrUpdateMember(list);
//
//            this.listChatRoom(pageNum + 1, limit);
//        }
//
//        return true;

        if (records.isEmpty()) {
            return true;
        }

        Set<ChatRoomModel> list = taskDomainService.listChatRoomDetail(new HashSet<>(records));
        if (list.isEmpty()) {
            return this.listChatRoom(pageNum + 1, limit);
        }

        this.saveOrUpdateChatRoom(list);

        // TODO 后期建议使用MQ异步调用
        this.saveOrUpdateMember(list);

        this.listChatRoom(pageNum + 1, limit);
        return true;
    }

    /**
     * 保存或更新群详情
     *
     * @param list
     * @return
     */
    private boolean saveOrUpdateChatRoom(Set<ChatRoomModel> list) {

        return chatRoomDomain.saveOrUpdateBatch(ChatRoom.create(list));
    }

    private boolean saveOrUpdateMember(Set<ChatRoomModel> list) {

        return memberDomain.saveOrUpdateBatch(ChatRoomTreeNode.createTreeNode(list));
    }

}

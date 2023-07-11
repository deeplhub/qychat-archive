package com.xh.qychat.domain.qychat.service.impl;

import com.xh.qychat.domain.qychat.model.ChatRoomTreeNode;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.domain.qychat.model.factory.MemberFactory;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomMemberEntity;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import com.xh.qychat.domain.qychat.repository.service.ChatRoomMemberService;
import com.xh.qychat.domain.qychat.repository.service.impl.MemberServiceImpl;
import com.xh.qychat.domain.qychat.service.MemberDomain;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
@Slf4j
@Service
public class MemberDomainImpl extends MemberServiceImpl implements MemberDomain {

    @Resource
    private ChatRoomMemberService chatRoomMemberService;

    @Override
    @Transactional
    public boolean saveOrUpdateBatch(ChatRoomTreeNode treeNode) {
        String chatId = treeNode.getChatId();

        List<MemberEntity> memberList = super.listByUserId(MemberFactory.getSingleton().listUserId(treeNode));
        List<MemberEntity> memberEntityList = MemberFactory.getSingleton().listMemberEntity(treeNode, memberList);
        if (memberEntityList.isEmpty()) return false;

        super.saveOrUpdateBatch(memberEntityList, CommonConstants.BATCH_SIZE);

        // 解除用户和群关系
        chatRoomMemberService.removeByChatId(chatId);

        Set<ChatRoomMemberEntity> chatRoomMemberSet = memberEntityList.parallelStream().map(o -> new ChatRoomMemberEntity(chatId, o.getUserId())).collect(Collectors.toSet());
        return chatRoomMemberService.saveBatch(chatRoomMemberSet, CommonConstants.BATCH_SIZE);
    }

    @Override
    @Transactional
    public boolean saveOrUpdateBatch(List<ChatRoomTreeNode> treeNodes) {
        if (treeNodes.isEmpty()) return false;

        Set<MemberEntity> memberSet = new HashSet<>();
        Set<ChatRoomMemberEntity> chatRoomMemberSet = new HashSet<>();
        Set<String> chatIds = new HashSet<>();

        treeNodes.parallelStream().forEach(treeNode -> {
            chatIds.add(treeNode.getChatId());

            List<MemberEntity> memberList = super.listByUserId(MemberFactory.getSingleton().listUserId(treeNode));
            memberList = MemberFactory.getSingleton().listMemberEntity(treeNode, memberList);

            memberSet.addAll(memberList);

            Set<ChatRoomMemberEntity> chatRoomMembers = treeNode.getChildren().parallelStream().map(o -> new ChatRoomMemberEntity(o.getChatId(), o.getUserid())).collect(Collectors.toSet());
            chatRoomMemberSet.addAll(chatRoomMembers);
        });

        if (!memberSet.isEmpty()) {
            super.saveOrUpdateBatch(memberSet, CommonConstants.BATCH_SIZE);
        }

        // 解除用户和群关系
        chatRoomMemberService.removeBatchByChatId(chatIds);

        return chatRoomMemberService.saveBatch(chatRoomMemberSet, CommonConstants.BATCH_SIZE);
    }


    @Override
    @Transactional
    public boolean saveOrUpdate(Member member) {
        MemberEntity entity = super.getByUserId(member.getUserId());
        if (entity == null) return false;
        return super.saveOrUpdate(MemberFactory.getSingleton().create(entity, member));
    }


}

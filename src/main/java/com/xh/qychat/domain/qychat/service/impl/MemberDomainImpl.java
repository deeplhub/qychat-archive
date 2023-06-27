package com.xh.qychat.domain.qychat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xh.qychat.domain.qychat.model.ChatRoomTreeNodeModel;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomMemberEntity;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import com.xh.qychat.domain.qychat.repository.service.ChatRoomMemberService;
import com.xh.qychat.domain.qychat.repository.service.impl.MemberServiceImpl;
import com.xh.qychat.domain.qychat.service.MemberDomain;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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
    public boolean saveOrUpdateBatch(Member member) {

        for (ChatRoomModel chatRoomModel : member.getChatRoomModelList()) {
            String chatId = chatRoomModel.getChatId();
            Set<ChatRoomMemberEntity> set = new HashSet<>();
            Set<MemberEntity> memberEntitySet = new HashSet<>();
            for (ChatRoomModel.RoomMemberModel roomMemberModel : chatRoomModel.getMemberList()) {
                String userid = roomMemberModel.getUserid();

                QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(MemberEntity::getUserId, userid);

                MemberEntity entity = super.getOne(queryWrapper);
                entity = (entity == null) ? new MemberEntity() : entity;

                if (roomMemberModel.getSign().equals(entity.getSign())) {
                    continue;
                }

                entity.setUserId(roomMemberModel.getUserid());
                entity.setName(roomMemberModel.getName());
                entity.setType(roomMemberModel.getType());
                entity.setSign(roomMemberModel.getSign());

                memberEntitySet.add(entity);

                ChatRoomMemberEntity chatRoomMemberEntity = new ChatRoomMemberEntity();
                chatRoomMemberEntity.setChatId(chatId);
                chatRoomMemberEntity.setUserId(userid);

                set.add(chatRoomMemberEntity);
            }

            super.saveOrUpdateBatch(memberEntitySet, 1000);
            chatRoomMemberService.dissolution(chatId, set.parallelStream().map(o -> o.getUserId()).collect(Collectors.toSet()));
            chatRoomMemberService.saveBatch(set, 1000);
        }


        return true;
    }

    @Override
    @Transactional
    public boolean saveOrUpdateBatch2(List<ChatRoomTreeNodeModel> treeNodeModel) {
        Set<MemberEntity> memberSet = new HashSet<>();
        Set<ChatRoomMemberEntity> chatRoomMemberSet = new HashSet<>();

        for (ChatRoomTreeNodeModel treeNode : treeNodeModel) {
            String chatId = treeNode.getChatId();

            Set<String> userIds = treeNode.getChildren().parallelStream().map(o -> o.getUserid()).collect(Collectors.toSet());

            List<MemberEntity> memberList = super.listByUserId(userIds);
            List<MemberEntity> memberEntityList = treeNode.getChildren().parallelStream().map(o -> this.getMemberEntity(o, memberList)).filter(Objects::nonNull).collect(Collectors.toList());

            memberSet.addAll(memberEntityList);

            chatRoomMemberSet.addAll(memberEntityList.parallelStream().map(o -> new ChatRoomMemberEntity(chatId, o.getUserId())).collect(Collectors.toSet()));
        }

        super.saveOrUpdateBatch(memberSet, 1000);
        treeNodeModel.parallelStream().forEach(item -> chatRoomMemberService.dissolution(item.getChatId(), item.getChildren().parallelStream().map(ChatRoomTreeNodeModel::getUserid).collect(Collectors.toSet())));

        chatRoomMemberService.saveBatch(chatRoomMemberSet, 1000);
        return true;
    }

    private MemberEntity getMemberEntity(ChatRoomTreeNodeModel treeNodeModel, List<MemberEntity> memberList) {
        Map<String, MemberEntity> entityMap = memberList.parallelStream().collect(Collectors.toMap(MemberEntity::getUserId, o -> o, (k1, k2) -> k2));

        MemberEntity entity = entityMap.get(treeNodeModel.getUserid());
        entity = (entity == null) ? new MemberEntity() : entity;

        if (treeNodeModel.getSign().equals(entity.getSign())) {
            return null;
        }

        entity.setUserId(treeNodeModel.getUserid());
        entity.setName(treeNodeModel.getName());
        entity.setType(treeNodeModel.getType());
        entity.setSign(treeNodeModel.getSign());

        return entity;
    }
}

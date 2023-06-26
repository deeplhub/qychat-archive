package com.xh.qychat.domain.qychat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomMemberEntity;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import com.xh.qychat.domain.qychat.repository.service.ChatRoomMemberService;
import com.xh.qychat.domain.qychat.repository.service.impl.MemberServiceImpl;
import com.xh.qychat.domain.qychat.service.MemberDomain;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
                entity.setCreateTime(DateUtil.date(roomMemberModel.getJoinTime()));

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
}

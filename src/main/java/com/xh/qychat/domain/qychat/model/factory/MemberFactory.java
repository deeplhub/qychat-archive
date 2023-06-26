package com.xh.qychat.domain.qychat.model.factory;

import cn.hutool.core.date.DateUtil;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomMemberEntity;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
public class MemberFactory {

    private Set<ChatRoomMemberEntity> chatRoomMemberList;

    private static class Inner {
        private static final MemberFactory instance = new MemberFactory();
    }

    private MemberFactory() {
    }

    public static MemberFactory getSingleton() {
        return MemberFactory.Inner.instance;
    }

    public Set<MemberEntity> createOrModifyEntity(Set<ChatRoomModel> chatRoomList, List<Member> memberList) {
        Map<String, Member> dictMap = memberList.parallelStream().collect(HashMap::new, (k, v) -> k.put(v.getChatId() + "_" + v.getUserid(), v), HashMap::putAll);
        this.chatRoomMemberList = new HashSet<>();

        Set<MemberEntity> memberEntitys = new HashSet<>();

        for (ChatRoomModel chatRoomModel : chatRoomList) {
            chatRoomModel.getMemberList().parallelStream().filter(o -> o != null).forEach(item -> {
                MemberEntity entity = this.getMemberEntity(dictMap, chatRoomModel, item);
                if (entity == null) return;

                memberEntitys.add(entity);

                ChatRoomMemberEntity chatRoomMemberEntity = new ChatRoomMemberEntity();
                chatRoomMemberEntity.setChatId(chatRoomModel.getChatId());
                chatRoomMemberEntity.setUserId(item.getUserid());
                this.chatRoomMemberList.add(chatRoomMemberEntity);
            });

        }

        return memberEntitys;
    }

    private MemberEntity getMemberEntity(Map<String, Member> dictMap, ChatRoomModel chatRoomModel, ChatRoomModel.RoomMemberModel roomMemberModel) {
        Member member = dictMap.get(chatRoomModel.getChatId() + "_" + roomMemberModel.getUserid());
        member = member == null ? new Member() : member;

        if (roomMemberModel.getSign().equals(member.getSign())) return null;

        MemberEntity entity = new MemberEntity();

        entity.setId(member.getMemberId());
        entity.setUserId(roomMemberModel.getUserid());
        entity.setName(roomMemberModel.getName());
        entity.setType(roomMemberModel.getType());
        entity.setSign(roomMemberModel.getSign());
        entity.setCreateTime(DateUtil.date(roomMemberModel.getJoinTime()));
        return entity;
    }


    public Set<ChatRoomMemberEntity> listChatRoomMember() {
        return chatRoomMemberList;
    }

    public Map<String, Set<String>> listChatRoomMemberTree() {
        return this.chatRoomMemberList.parallelStream().collect(Collectors.groupingBy(item -> item.getChatId(), Collectors.mapping(item -> item.getUserId(), Collectors.toSet())));
    }
}

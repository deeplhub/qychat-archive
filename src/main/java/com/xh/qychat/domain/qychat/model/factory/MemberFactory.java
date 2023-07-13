package com.xh.qychat.domain.qychat.model.factory;

import com.xh.qychat.domain.qychat.model.ChatRoomTreeNode;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
public class MemberFactory {

    private static class Inner {
        private static final MemberFactory instance = new MemberFactory();
    }

    private MemberFactory() {
    }

    public static MemberFactory getSingleton() {
        return MemberFactory.Inner.instance;
    }


    public Set<String> listUserId(ChatRoomTreeNode treeNode) {

        return treeNode.getChildren().parallelStream().map(o -> o.getUserid()).collect(Collectors.toSet());
    }

    public List<MemberEntity> listMemberEntity(ChatRoomTreeNode treeNode, List<MemberEntity> memberList) {

        return treeNode.getChildren().parallelStream().map(o -> this.getMemberEntity(o, memberList)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public MemberEntity getMemberEntity(ChatRoomTreeNode treeNodeModel, List<MemberEntity> memberList) {
        Map<String, MemberEntity> entityMap = memberList.parallelStream().collect(Collectors.toMap(MemberEntity::getUserId, o -> o, (k1, k2) -> k2));

        MemberEntity entity = entityMap.get(treeNodeModel.getUserid());
        entity = (entity == null) ? new MemberEntity() : entity;

        if (treeNodeModel.getSign().equals(entity.getSign())) return null;

        entity.setUserId(treeNodeModel.getUserid());
        entity.setName(treeNodeModel.getName());
        entity.setType(treeNodeModel.getType());

        return entity;
    }

    public MemberEntity create(MemberEntity entity, Member member) {

        entity.setUserId(member.getUserId());
        entity.setAvatar(member.getAvatar());
        entity.setGender(member.getGender());
        entity.setPosition(member.getPosition());
        entity.setTelephone(member.getTelephone());

        return entity;

    }


    public Member toMember(MemberEntity entity) {
        Member member = new Member();

        member.setId(entity.getId() + "");
        member.setUserId(entity.getUserId());
        member.setName(entity.getName());
        member.setAvatar(entity.getAvatar());
        member.setType(entity.getType());
        member.setGender(entity.getGender());

        return member;
    }
}

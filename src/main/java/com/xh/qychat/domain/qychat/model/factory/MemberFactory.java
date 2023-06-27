package com.xh.qychat.domain.qychat.model.factory;

import com.xh.qychat.domain.qychat.model.ChatRoomTreeNode;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;

import java.util.List;
import java.util.Map;
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


    public MemberEntity getMemberEntity(ChatRoomTreeNode treeNodeModel, List<MemberEntity> memberList) {
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

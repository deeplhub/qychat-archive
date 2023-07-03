package com.xh.qychat.domain.qychat.service;

import com.xh.qychat.domain.qychat.model.ChatRoomTreeNode;
import com.xh.qychat.domain.qychat.model.Member;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
public interface MemberDomain {

    boolean saveOrUpdateBatch(ChatRoomTreeNode treeNode);

    boolean saveOrUpdateBatch(List<ChatRoomTreeNode> treeNodes);

    boolean saveOrUpdate(Member member);
}

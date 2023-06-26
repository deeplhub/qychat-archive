package com.xh.qychat.domain.qychat.service;

import com.xh.qychat.domain.qychat.model.ChatRoomTreeNodeModel;
import com.xh.qychat.domain.qychat.model.Member;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
public interface MemberDomain {

    boolean saveOrUpdateBatch(Member member);

    boolean saveOrUpdateBatch2(List<ChatRoomTreeNodeModel> treeNodeModel);
}

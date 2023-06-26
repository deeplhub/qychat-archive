package com.xh.qychat.domain.qychat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.domain.qychat.model.factory.MemberFactory;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomMemberEntity;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import com.xh.qychat.domain.qychat.repository.service.ChatRoomMemberService;
import com.xh.qychat.domain.qychat.repository.service.impl.MemberServiceImpl;
import com.xh.qychat.domain.qychat.service.MemberDomain;
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
        // TODO 这里只能一个群一个群的遍历，如果一次性处理会造成用户重复
        Set<String> chatIds = member.listChatId();
        List<Member> memberList = super.listByCharId(chatIds);
        Set<MemberEntity> entityList = MemberFactory.getSingleton().createOrModifyEntity(member.getChatRoomModelList(), memberList);
        if (entityList.isEmpty()) {
            return true;
        }

        log.info("entityList : {}", JSONUtil.toJsonStr(entityList));

        super.saveOrUpdateBatch(entityList, 1000);

        Map<String, Set<String>> dbMap = chatRoomMemberService.listByChatId(chatIds)
                .parallelStream().collect(Collectors.groupingBy(item -> item.getChatId(), Collectors.mapping(item -> item.getUserId(), Collectors.toSet())));
        Map<String, Set<String>> map = MemberFactory.getSingleton().listChatRoomMemberTree();

        Set<ChatRoomMemberEntity> roomMemberList = new HashSet<>();
        Map<String, Object> columnMap = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            String entryKey = entry.getKey();
            boolean isEqual = CollUtil.isEqualList(entry.getValue(), dbMap.get(entryKey));

            if (!isEqual) {
                columnMap.put("chat_id", entryKey);

                for (String userId : entry.getValue()) {
                    ChatRoomMemberEntity entity = new ChatRoomMemberEntity();
                    entity.setChatId(entryKey);
                    entity.setUserId(userId);

                    roomMemberList.add(entity);
                }

            }
        }

        if (columnMap.isEmpty()) {
            return true;
        }

        log.info("columnMap : {}", JSONUtil.toJsonStr(columnMap));
        log.info("roomMemberList : {}", JSONUtil.toJsonStr(roomMemberList));

        chatRoomMemberService.removeByMap(columnMap);
        return chatRoomMemberService.saveBatch(roomMemberList, 1000);

    }
}

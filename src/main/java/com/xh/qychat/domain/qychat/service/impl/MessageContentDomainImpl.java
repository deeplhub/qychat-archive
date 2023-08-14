package com.xh.qychat.domain.qychat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.model.factory.MessageContentFactory;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.service.MemberService;
import com.xh.qychat.domain.qychat.repository.service.MessageContentService;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Slf4j
@Service
public class MessageContentDomainImpl implements MessageContentDomain {

    @Resource
    private MessageContentService messageContentService;
    @Resource
    private MemberService memberService;

    @Override
    public Long getMaxSeq() {
        // TODO 最大序列未来可以选择从缓存中读取
        Long maxseq = messageContentService.getMaxSeq();
        return maxseq == null ? 0 : maxseq;
    }

    @Override
    @Transactional
    public boolean saveBath(List<MessageContent> messageContents) {
        if (messageContents.isEmpty()) return false;
        List<MessageContentEntity> entitys = MessageContentFactory.getSingleton().createEntity(messageContents);
        return messageContentService.saveBatch(entitys, CommonConstants.BATCH_SIZE);
    }

    @Override
    public Page<String> pageListRoomIdGoupByRoomId(Integer pageNum, Integer limit) {
        return messageContentService.pageListRoomIdGoupByRoomId(pageNum, limit);
    }

    @Override
    public Map<String, Object> listByChatId(String chatId, String msgtime) {
        List<MessageContentEntity> entityList = messageContentService.listByChatId(chatId, msgtime);
        if (entityList.isEmpty()) {
            MessageContentEntity entity = messageContentService.getByChatId(chatId, msgtime);
            if (entity != null) {
                msgtime = DateUtil.formatDate(entity.getMsgtime());
                entityList = messageContentService.listByChatId(chatId, DateUtil.formatDate(entity.getMsgtime()));
            }
        }
        Map<String, Object> responseMap = new HashMap<>();

        if (entityList.isEmpty()) {
            responseMap.put("msgtime", msgtime);
            responseMap.put("messageList", new ArrayList<>());
            return responseMap;
        }

        List<MemberEntity> memberList = memberService.listByCharId(chatId);
        Map<String, MemberEntity> memberMap = memberList.parallelStream().collect(HashMap::new, (k, v) -> k.put(v.getUserId(), v), HashMap::putAll);

        responseMap.put("msgtime", msgtime);
        responseMap.put("messageList", entityList.parallelStream().map(o -> MessageContentFactory.getSingleton().toMessageContent(o, memberMap)).collect(Collectors.toList()));

        return responseMap;
    }

}

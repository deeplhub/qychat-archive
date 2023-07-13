package com.xh.qychat.domain.qychat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.model.factory.MessageContentFactory;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.service.MessageContentService;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
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
    public List<MessageContent> pageListByChatId(String chatId, Integer pageNum, Integer limit) {
        Page<MessageContentEntity> page = messageContentService.pageListByChatId(chatId, pageNum, limit);
        return page.getRecords().parallelStream().map(o -> MessageContentFactory.getSingleton().toMessageContent(o)).collect(Collectors.toList());
    }


}

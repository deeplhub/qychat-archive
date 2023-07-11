package com.xh.qychat.domain.qychat.service.impl;

import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.model.factory.MessageContentFactory;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.service.impl.MessageContentServiceImpl;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Slf4j
@Service
public class MessageContentDomainImpl extends MessageContentServiceImpl implements MessageContentDomain {

    @Override
    public Long getMaxSeq() {
        // TODO 最大序列未来可以选择从缓存中读取
        Long maxseq = super.getMaxSeq();
        return maxseq == null ? 0 : maxseq;
    }

    @Override
    @Transactional
    public boolean saveBath(List<MessageContent> messageContents) {
        if (messageContents.isEmpty()) return false;
        List<MessageContentEntity> entitys = MessageContentFactory.getSingleton().createEntity(messageContents);
        return super.saveBatch(entitys, CommonConstants.BATCH_SIZE);
    }


}

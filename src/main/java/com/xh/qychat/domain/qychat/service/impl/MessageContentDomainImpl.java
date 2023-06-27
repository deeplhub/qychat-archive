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
        Long maxseq = super.getMaxSeq();
        return maxseq == null ? 0 : maxseq;
    }

    @Override
    @Transactional
    public boolean saveBath(MessageContent messageContent) {
        List<MessageContentEntity> entityList = MessageContentFactory.createEntity(messageContent);
        if (entityList.isEmpty()) {
            log.warn("保存消息内容出现错误，数据为空");
            return true;
        }
        return super.saveBatch(entityList, CommonConstants.BATCH_SIZE);
    }

}

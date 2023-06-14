package com.xh.qychat.domain.qychat.service.impl;

import com.xh.qychat.domain.qychat.factory.MessageFactory;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.service.impl.MessageContentServiceImpl;
import com.xh.qychat.domain.qychat.service.MessageContentDomainService;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Slf4j
@Service
public class MessageContentDomainServiceImpl extends MessageContentServiceImpl implements MessageContentDomainService {


    @Override
    public Long getMaxSeq() {
        Long maxseq = super.getMaxSeq();
        return maxseq == null ? 0 : maxseq;
    }

    @Override
    public boolean saveBath(List<ChatDataModel> dataModelList) {
        List<MessageContentEntity> entity = MessageFactory.createMessageContentEntity(dataModelList);

        return super.saveBatch(entity, 1000);
    }
}

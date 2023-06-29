package com.xh.qychat.domain.qychat.service.impl;

import com.xh.qychat.domain.qychat.model.factory.MessageContentFactory;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.service.impl.MessageContentServiceImpl;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
import com.xh.qychat.infrastructure.common.enums.ResponseEnum;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
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
    public boolean saveBath(List<ChatDataModel> dataModels) {
        if (dataModels.isEmpty()) throw new RuntimeException(ResponseEnum.REQUEST_PARAMETERS.getNote());
        List<MessageContentEntity> entitys = MessageContentFactory.getSingleton().createEntity(dataModels);
        boolean isSuccess = super.saveBatch(entitys, CommonConstants.BATCH_SIZE);
        // 事务提交后执行扩展逻辑
//        if (isSuccess) TransactionSynchronizationManager.registerSynchronization(new ChatDataTransactionCommitEvent(entitys));
        return isSuccess;
    }


}

package com.xh.qychat.domain.qychat.event;

import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/28
 */
@Slf4j
public class ChatDataTransactionCommitEvent extends TransactionSynchronizationAdapter {

    private List<MessageContentEntity> messageContentList;

    public ChatDataTransactionCommitEvent(List<MessageContentEntity> messageContentList) {
        this.messageContentList = messageContentList;
    }


    @Override
    public void afterCommit() {
        for (MessageContentEntity entity : messageContentList) {
            MessageStrategy strategy = SpringBeanUtils.getBean(entity.getMsgtype() + "MessageStrategyImpl");
            if (strategy != null) {
                strategy.process(entity);

//                messageContentService.updateById(entity.getContent(), entity.getId());
            }
        }
    }

}

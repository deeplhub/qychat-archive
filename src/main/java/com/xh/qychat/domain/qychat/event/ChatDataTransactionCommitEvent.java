package com.xh.qychat.domain.qychat.event;

import cn.hutool.core.util.StrUtil;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.service.MessageContentService;
import com.xh.qychat.domain.qychat.repository.service.impl.MessageContentServiceImpl;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import java.util.List;

/**
 * 事务同步适配器
 * <p>
 * 事务提交后执行逻辑
 *
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
        AsyncCommitEvent asyncCommitEvent = SpringBeanUtils.getBean(AsyncCommitEvent.class);
        MessageContentService messageContentService = SpringBeanUtils.getBean(MessageContentServiceImpl.class);
        for (MessageContentEntity entity : messageContentList) {
            if (StrUtil.isBlank(entity.getMsgtype()) || StrUtil.isBlank(entity.getContent())) continue;

            asyncCommitEvent.getMessageCentent(entity, messageContentService);
        }

    }

}

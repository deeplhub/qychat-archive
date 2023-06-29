package com.xh.qychat.domain.qychat.event;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.model.ChatDataMessage;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.service.MessageContentService;
import com.xh.qychat.domain.qychat.repository.service.impl.MessageContentServiceImpl;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.infrastructure.config.CustomizedTaskExecutor;
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

    private final CustomizedTaskExecutor customizedTaskExecutor = SpringBeanUtils.getBean(CustomizedTaskExecutor.class);
    private final MessageContentService messageContentService = SpringBeanUtils.getBean(MessageContentServiceImpl.class);

    private List<MessageContentEntity> messageContentList;

    public ChatDataTransactionCommitEvent(List<MessageContentEntity> messageContentList) {
        this.messageContentList = messageContentList;
    }


    @Override
    public void afterCommit() {
        // TODO 验证一下这里是一个异步线程还是多个异步线程
        customizedTaskExecutor.execute(() -> {
            for (MessageContentEntity entity : messageContentList) {
                MessageStrategy strategy = SpringBeanUtils.getBean(entity.getMsgtype() + "MessageStrategyImpl");
                if (strategy == null && StrUtil.isBlank(entity.getContent())) continue;

                ChatDataMessage chatDataMessage = new ChatDataMessage();
                chatDataMessage.setContent(entity.getContent());
                chatDataMessage.setType(entity.getMsgtype());

                chatDataMessage = chatDataMessage.create();

                log.debug("消息ID：[{}], 消息请求：{}", entity.getId(), JSONUtil.toJsonStr(chatDataMessage));
                String content = strategy.process(chatDataMessage);
                log.debug("消息ID：[{}], 媒体状态：[{}], 消息策略返回结果：{}", entity.getId(), chatDataMessage.getMediaStatus(), content);

                messageContentService.updateById(content, chatDataMessage.getMediaStatus(), entity.getId());
            }
        });
    }

}

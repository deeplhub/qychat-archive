package com.xh.qychat.domain.qychat.event;

import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.model.ChatDataMessage;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.service.MessageContentService;
import com.xh.qychat.domain.qychat.service.adapter.MessageAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author H.Yang
 * @date 2023/6/29
 */
@Slf4j
@Service
public class AsyncCommitEvent {

    @Async("customizedTaskExecutor")
    public void getMessageCentent(MessageContentEntity entity, MessageContentService messageContentService) {
        ChatDataMessage chatDataMessage = new ChatDataMessage();
        chatDataMessage.setContent(entity.getContent());
        chatDataMessage.setType(entity.getMsgtype());

        chatDataMessage = chatDataMessage.create();

        MessageAdapter messageAdapter = new MessageAdapter(entity.getMsgtype());

        log.debug("消息ID：[{}], 消息请求：{}", entity.getMsgid(), JSONUtil.toJsonStr(chatDataMessage));
        String content = messageAdapter.getChatDataMessage(chatDataMessage);
        log.debug("消息ID：[{}], 消息策略返回结果：{}", entity.getMsgid(), content);

        entity.setContent(content);
        entity.setMediaStatus(chatDataMessage.getMediaStatus());

        messageContentService.updateById(content, chatDataMessage.getMediaStatus(), entity.getId());
    }
}

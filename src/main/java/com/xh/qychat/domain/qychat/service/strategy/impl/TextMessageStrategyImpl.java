package com.xh.qychat.domain.qychat.service.strategy.impl;

import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.domain.qychat.service.strategy.dto.ChatDataMessageDTO;

/**
 * 文本消息
 * MarkDown格式消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class TextMessageStrategyImpl implements MessageStrategy {

    @Override
    public String process(ChatDataMessageDTO chatDataDto) {

        return chatDataDto.getContent();
    }
}

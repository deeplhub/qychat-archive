package com.xh.qychat.domain.qychat.event.strategy;

import com.xh.qychat.domain.qychat.event.strategy.dto.ChatDataMessageDTO;

/**
 * 文本消息
 * MarkDown格式消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class TextMessageStrategy implements MessageStrategy {

    @Override
    public String process(ChatDataMessageDTO chatDataDto) {

        return chatDataDto.getContent();
    }
}

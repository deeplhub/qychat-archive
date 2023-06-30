package com.xh.qychat.domain.qychat.service.strategy.impl;

import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.domain.qychat.service.strategy.dto.ChatDataMessageDTO;

/**
 * 音视频通话
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class VoiptextMessageStrategyImpl implements MessageStrategy {

    @Override
    public String process(ChatDataMessageDTO chatDataDto) {

        return chatDataDto.getVoipText();
    }
}

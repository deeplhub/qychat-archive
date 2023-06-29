package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.json.JSONObject;
import com.xh.qychat.domain.qychat.model.ChatDataMessage;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;

/**
 * 文本消息
 * MarkDown格式消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class TextMessageStrategyImpl implements MessageStrategy {

    @Override
    public String process(ChatDataMessage chatDataMessage) {
        JSONObject contentObject = chatDataMessage.getContentObject();

        return contentObject.getStr("content");
    }
}

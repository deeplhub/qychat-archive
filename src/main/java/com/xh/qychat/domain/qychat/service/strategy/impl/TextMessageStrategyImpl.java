package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.json.JSONObject;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import org.springframework.stereotype.Component;

/**
 * 文本消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
@Component
public class TextMessageStrategyImpl implements MessageStrategy {

    @Override
    public void process(ChatDataModel model, MessageContentEntity entity) {
        JSONObject jsonObject = model.getText();

        entity.setContent(jsonObject.getStr("content"));
    }
}

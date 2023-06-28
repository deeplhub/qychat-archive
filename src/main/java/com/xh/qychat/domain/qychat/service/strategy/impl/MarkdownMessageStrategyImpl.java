package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.json.JSONObject;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.service.strategy.NormalMessageStrategy;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import org.springframework.stereotype.Component;

/**
 * MarkDown格式消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
@Component
public class MarkdownMessageStrategyImpl implements NormalMessageStrategy {

    @Override
    public void process(ChatDataModel model, MessageContentEntity entity) {
        JSONObject jsonObject = model.getInfo();

        entity.setContent(jsonObject.getStr("content"));
    }
}

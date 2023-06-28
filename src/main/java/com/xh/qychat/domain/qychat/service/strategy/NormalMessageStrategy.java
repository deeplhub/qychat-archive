package com.xh.qychat.domain.qychat.service.strategy;

import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;

/**
 * @author H.Yang
 * @date 2023/6/28
 */
public interface NormalMessageStrategy {


    void process(ChatDataModel model, MessageContentEntity entity);
}

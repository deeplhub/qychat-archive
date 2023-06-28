package com.xh.qychat.domain.qychat.service.strategy;

import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;

/**
 * 消息格式策略
 * <p>
 * https://developer.work.weixin.qq.com/document/path/91774
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public interface MessageStrategy {

    void process(MessageContentEntity entity);
}

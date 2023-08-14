package com.xh.qychat.application.service;

import com.xh.qychat.infrastructure.common.model.Result;

/**
 * @author H.Yang
 * @date 2023/7/13
 */
public interface MessageContentApplication {

    /**
     * 根据群ID分页查询会话消息
     *
     * @param chatId
     * @param msgtime
     * @return
     */
    Result listByChatId(String chatId, String msgtime);
}

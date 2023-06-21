package com.xh.qychat.domain.qychat.service;

import com.xh.qychat.domain.qychat.model.MessageContent;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface MessageContentDomainService {

    /**
     * 获取最大索引
     */
    Long getMaxSeq();

    boolean saveBath(MessageContent messageContent);
}

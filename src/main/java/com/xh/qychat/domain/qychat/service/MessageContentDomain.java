package com.xh.qychat.domain.qychat.service;

import com.xh.qychat.domain.qychat.model.MessageContent;

import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface MessageContentDomain {

    /**
     * 获取最大索引
     */
    Long getMaxSeq();

    boolean saveBath(MessageContent messageContent);

    /**
     * 获取所有群ID
     *
     * @return
     */
    Set<String> listRoomIdGoupByRoomId();
}

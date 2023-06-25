package com.xh.qychat.application.service;

import com.xh.qychat.infrastructure.common.model.Result;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface TaskApplication {

    /**
     * 拉取聊天数据
     *
     * @return
     */
    Result pullChatData();

    /**
     * 拉取群详情
     *
     * @return
     */
    Result pullChatRoom();
}

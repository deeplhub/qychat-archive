package com.xh.qychat.application.service;

import com.xh.qychat.infrastructure.common.model.Result;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface TaskApplication {

    Result pullChatData();

    Result pullChatRoom();
}

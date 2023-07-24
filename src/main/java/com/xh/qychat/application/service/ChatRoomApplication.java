package com.xh.qychat.application.service;

import com.xh.qychat.infrastructure.common.model.Result;

/**
 * @author H.Yang
 * @date 2023/7/12
 */
public interface ChatRoomApplication {

    /**
     * 拉取指定群详情
     * <p>
     * 包含群下的所有成员的基本信息
     *
     * @param chatId 群ID
     * @return
     */
    Result pullChatRoom(String chatId);

    /**
     * 查询所有群列表
     *
     * @return
     */
    Result listChatRoom();

}

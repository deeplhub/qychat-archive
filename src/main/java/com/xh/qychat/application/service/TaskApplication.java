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
     * <p>
     * 包含群下的所有成员的基本信息
     *
     * @return
     */
    Result pullChatRoom();

    /**
     * 拉取群详情
     * <p>
     * 包含群下的所有成员的基本信息
     *
     * @param roomId 群ID
     * @return
     */
    Result pullChatRoom(String roomId);

    /**
     * 拉取人员详情
     *
     * @param userId 人员ID
     * @return
     */
    Result pullPersonnel(String userId);
}

package com.xh.qychat.application.service;

import com.xh.qychat.infrastructure.common.model.Result;

/**
 * @author H.Yang
 * @date 2023/7/13
 */
public interface MemberApplication {

    /**
     * 拉取人员详情
     *
     * @param userId 人员ID
     * @return
     */
    Result pullPersonnel(String userId);

    /**
     * 根据群ID查询成员列表
     *
     * @param chatId
     * @return
     */
    Result listByChatId(String chatId);

}

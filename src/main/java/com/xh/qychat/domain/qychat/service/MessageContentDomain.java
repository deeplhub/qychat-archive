package com.xh.qychat.domain.qychat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.qychat.domain.qychat.model.MessageContent;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface MessageContentDomain {

    /**
     * 获取会话消息最大索引
     */
    Long getMaxSeq();

    boolean saveBath(List<MessageContent> messageContents);

    /**
     * 分页查询群ID
     *
     * @param pageNum
     * @param limit
     * @return
     */
    Page<String> pageListRoomIdGoupByRoomId(Integer pageNum, Integer limit);


    /**
     * 根据群ID查询消息
     *
     * @param chatId
     * @return
     */
    List<MessageContent> pageListByChatId(String chatId, Integer pageNum, Integer limit);
}

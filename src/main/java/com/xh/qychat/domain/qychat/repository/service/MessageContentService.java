package com.xh.qychat.domain.qychat.repository.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;

import java.util.List;
import java.util.Set;

public interface MessageContentService extends IService<MessageContentEntity> {

    /**
     * 获取最大索引
     */
    Long getMaxSeq();

    /**
     * 分页查询群ID
     *
     * @param pageNum
     * @param limit
     * @return
     */
    Page<String> pageListRoomIdGoupByRoomId(Integer pageNum, Integer limit);

    /**
     * 根据群ID查询会话消息
     */
    List<MessageContentEntity> listByChatId(String chatId, Integer seq);
}

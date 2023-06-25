package com.xh.qychat.domain.qychat.repository.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;

public interface MessageContentService extends IService<MessageContentEntity> {

    /**
     * 获取最大索引
     */
    Long getMaxSeq();

    Page<String> pageListRoomIdGoupByRoomId(Integer pageNum, Integer limit);

}

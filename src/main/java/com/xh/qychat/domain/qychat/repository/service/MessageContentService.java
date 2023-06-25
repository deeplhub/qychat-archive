package com.xh.qychat.domain.qychat.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;

import java.util.Set;

public interface MessageContentService extends IService<MessageContentEntity> {

    /**
     * 获取最大索引
     */
    Long getMaxSeq();

    Set<String> listRoomIdGoupByRoomId();
}

package com.xh.qychat.domain.qychat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface MessageContentDomain {

    /**
     * 获取最大索引
     */
    Long getMaxSeq();

    boolean saveBath(List<ChatDataModel> dataModels);

    /**
     * 获取所有群ID
     *
     * @param pageNum
     * @param limit
     * @return
     */
    Page<String> pageListRoomIdGoupByRoomId(Integer pageNum, Integer limit);

}

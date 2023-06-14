package com.xh.qychat.domain.qychat.service;

import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface MessageContentDomainService {

    /**
     * 获取最大索引
     */
    Long getMaxSeq();

    boolean saveBath(List<ChatDataModel> dataModelList);
}

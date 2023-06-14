package com.xh.qychat.domain.qychat.factory;

import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.strategy.ConvertorStrategy;
import org.mapstruct.Mapper;

/**
 * @author H.Yang
 * @date 2021/8/10
 */
@Mapper(componentModel = "spring", uses = ConvertorStrategy.class)// uses 自定义注射策略
public interface QyChatConvertorFactory {
    MessageContentEntity chatDataModelToMessageContentEntity(ChatDataModel chatDataModel);
}

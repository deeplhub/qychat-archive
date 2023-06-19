package com.xh.qychat.domain.qychat.event.mapper;

import com.xh.qychat.infrastructure.strategy.ConvertorStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 映射器接口
 * <p>
 * 手动调用：Mappers.getMapper(QyChatMapStructMapper.class) 来获取MapStruct中mapper的实例
 * 使用依赖注入：@Mapper(componentModel = "spring")
 *
 * @author H.Yang
 * @date 2021/8/10
 */
@Mapper(componentModel = "spring", uses = ConvertorStrategy.class)// uses 自定义注射策略
public interface QyChatMapStructMapper {
    QyChatMapStructMapper INSTANCE = Mappers.getMapper(QyChatMapStructMapper.class);
}

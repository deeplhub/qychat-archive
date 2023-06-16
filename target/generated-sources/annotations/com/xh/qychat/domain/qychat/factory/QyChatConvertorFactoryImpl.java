package com.xh.qychat.domain.qychat.factory;

import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-16T10:05:37+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 1.8.0_271 (Oracle Corporation)"
)
@Component
public class QyChatConvertorFactoryImpl implements QyChatConvertorFactory {

    @Override
    public MessageContentEntity chatDataModelToMessageContentEntity(ChatDataModel chatDataModel) {
        if ( chatDataModel == null ) {
            return null;
        }

        MessageContentEntity messageContentEntity = new MessageContentEntity();

        return messageContentEntity;
    }
}

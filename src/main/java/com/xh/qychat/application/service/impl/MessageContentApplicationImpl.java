package com.xh.qychat.application.service.impl;

import com.xh.qychat.application.service.MessageContentApplication;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
import com.xh.qychat.infrastructure.common.model.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author H.Yang
 * @date 2023/7/13
 */
@Service
public class MessageContentApplicationImpl implements MessageContentApplication {

    @Resource
    private MessageContentDomain messageContentDomain;

    @Override
    public Result listByChatId(String chatId, Integer seq) {
        List<MessageContent> list = messageContentDomain.listByChatId(chatId, seq);
        return Result.succeed(list);
    }

}

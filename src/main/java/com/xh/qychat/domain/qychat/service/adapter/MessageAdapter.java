package com.xh.qychat.domain.qychat.service.adapter;

import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.domain.qychat.service.strategy.dto.ChatDataMessageDTO;
import com.xh.qychat.domain.qychat.service.strategy.impl.ChatrecordMessageStrategyImpl;
import com.xh.qychat.domain.qychat.service.strategy.impl.MediaMessageStrategyImpl;
import com.xh.qychat.domain.qychat.service.strategy.impl.MixedMessageStrategyImpl;
import com.xh.qychat.domain.qychat.service.strategy.impl.TextMessageStrategyImpl;

/**
 * 消息格式适配器
 *
 * @author H.Yang
 * @date 2023/6/29
 */
public class MessageAdapter {

    private MessageStrategy messageStrategy;

    public MessageAdapter(String msgType) {
        switch (msgType) {
            case "text":
            case "markdown":
                messageStrategy = new TextMessageStrategyImpl();
                break;
            case "image":
            case "voice":
            case "video":
            case "file":
            case "emotion":
                messageStrategy = new MediaMessageStrategyImpl();
                break;
            case "mixed":
                messageStrategy = new MixedMessageStrategyImpl();
                break;
            case "chatrecord":
                messageStrategy = new ChatrecordMessageStrategyImpl();
                break;
            default:
                break;
        }
    }


    public String getChatDataMessage(ChatDataMessageDTO chatDataDto) {

        return (messageStrategy != null) ? messageStrategy.process(chatDataDto) : null;
    }

}

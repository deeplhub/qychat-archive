package com.xh.qychat.domain.qychat.event.adapter;

import com.xh.qychat.domain.qychat.event.strategy.*;
import com.xh.qychat.domain.qychat.event.strategy.dto.ChatDataMessageDTO;

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
                messageStrategy = new TextMessageStrategy();
                break;
            case "image":
            case "voice":
            case "video":
            case "file":
            case "emotion":
                messageStrategy = new MediaMessageStrategy();
                break;
            case "mixed":
                messageStrategy = new MixedMessageStrategy();
                break;
            case "chatrecord":
                messageStrategy = new ChatrecordMessageStrategy();
                break;
            case "voiptext":
                messageStrategy = new VoiptextMessageStrategy();
                break;
            default:
                break;
        }
    }


    public String getChatDataMessage(ChatDataMessageDTO chatDataDto) {

        return (messageStrategy != null) ? messageStrategy.process(chatDataDto) : null;
    }

}

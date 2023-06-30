package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.service.adapter.MessageAdapter;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.domain.qychat.service.strategy.dto.ChatDataMessageDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 会话记录消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class ChatrecordMessageStrategyImpl implements MessageStrategy {

    @Override
    public String process(ChatDataMessageDTO chatDataDto) {
        List<ChatDataMessageDTO> items = chatDataDto.getItem();
        List<JSONObject> chatDataList = items.parallelStream().map(this::getAction).filter(Objects::nonNull).collect(Collectors.toList());

        JSONObject data = new JSONObject();

        data.putOpt("title", chatDataDto.getTitle());
        data.putOpt("item", chatDataList);

        return data.toString();
    }


    public JSONObject getAction(ChatDataMessageDTO chatDataDto) {
        switch (chatDataDto.getType()) {
            case "text":
            case "markdown":
                return this.getChatRecordText(chatDataDto);
            case "image":
            case "voice":
            case "video":
            case "file":
            case "emotion":
                return this.getChatRecordMedia(chatDataDto);
            case "mixed":
                return this.getChatRecordMixed(chatDataDto);
            default:
                break;
        }
        return null;
    }

    private JSONObject getChatRecordText(ChatDataMessageDTO chatrecordDto) {
        String content = chatrecordDto.createMixed().getContent();

        JSONObject data = new JSONObject();

        data.putOpt("type", chatrecordDto.getType());
        data.putOpt("content", content);
        data.putOpt("fromChatroom", chatrecordDto.getFromChatroom());
        data.putOpt("msgtime", chatrecordDto.getMsgtime());

        return data;
    }

    private JSONObject getChatRecordMedia(ChatDataMessageDTO chatrecordDto) {
        ChatDataMessageDTO mediaDto = chatrecordDto.createMixed();
        mediaDto.setMsgType(chatrecordDto.getType());

        MessageAdapter messageAdapter = new MessageAdapter(chatrecordDto.getType());
        String content = messageAdapter.getChatDataMessage(mediaDto);

        JSONObject data = JSONUtil.parseObj(content);

        data.putOpt("type", chatrecordDto.getType());
        data.putOpt("fromChatroom", chatrecordDto.getFromChatroom());
        data.putOpt("msgtime", chatrecordDto.getMsgtime());

        return data;
    }

    private JSONObject getChatRecordMixed(ChatDataMessageDTO chatrecordDto) {
        ChatDataMessageDTO mixedDto = chatrecordDto.createMixed();
        mixedDto.setMsgType(chatrecordDto.getType());

        MessageAdapter messageAdapter = new MessageAdapter(chatrecordDto.getType());
        String content = messageAdapter.getChatDataMessage(mixedDto);

        JSONObject data = new JSONObject();

        data.putOpt("item", JSONUtil.parseArray(content));
        data.putOpt("type", chatrecordDto.getType());
        data.putOpt("fromChatroom", chatrecordDto.getFromChatroom());
        data.putOpt("msgtime", chatrecordDto.getMsgtime());

        return data;
    }
}

package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.model.ChatDataMessage;
import com.xh.qychat.domain.qychat.service.adapter.MessageAdapter;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 会话记录消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class ChatrecordMessageStrategyImpl implements MessageStrategy {

    private final Map<String, Function<JSONObject, JSONObject>> actionMappings = new HashMap<>();

    {
        actionMappings.put("ChatRecordText", o -> this.getText(o));
        actionMappings.put("ChatRecordImage", o -> this.getMedia(o));
        actionMappings.put("ChatRecordVoice", o -> this.getMedia(o));
        actionMappings.put("ChatRecordVideo", o -> this.getMedia(o));
        actionMappings.put("ChatRecordFile", o -> this.getMedia(o));
        actionMappings.put("ChatRecordEmotion", o -> this.getMedia(o));
        actionMappings.put("ChatRecordMixed", o -> this.getMixed(o));
    }


    @Override
    public String process(ChatDataMessage chatDataMessage) {
        JSONObject jsonObject = chatDataMessage.getContentObject();
        String title = jsonObject.getStr("title");

        List<JSONObject> item = jsonObject.getBeanList("item", JSONObject.class);

        List<JSONObject> chatDataMessageList = item.parallelStream().map(this::getAction).filter(Objects::nonNull).collect(Collectors.toList());

        jsonObject.set("item", chatDataMessageList);
        return jsonObject.toString();
    }

    private JSONObject getAction(JSONObject jsonObject) {
        String type = jsonObject.getStr("type");
        Function<JSONObject, JSONObject> function = actionMappings.get(type);
        if (function == null) return null;

        return function.apply(jsonObject);
    }


    private JSONObject getText(JSONObject jsonObject) {
        String content = jsonObject.getJSONObject("content").getStr("content");
        jsonObject.putOpt("type", "text");
        jsonObject.set("content", content);

        return jsonObject;
    }

    private JSONObject getMedia(JSONObject jsonObject) {
        ChatDataMessage chatDataMessage = new ChatDataMessage();
        chatDataMessage.setContent(jsonObject.getStr("content"));
        chatDataMessage.setType(jsonObject.getStr("type"));

        chatDataMessage = chatDataMessage.create();

        MessageAdapter messageAdapter = new MessageAdapter(chatDataMessage.getType());

        String content = messageAdapter.getChatDataMessage(chatDataMessage);

        jsonObject.remove("content");
        jsonObject.putAll(JSONUtil.parseObj(content));
        return jsonObject;
    }

    private JSONObject getMixed(JSONObject jsonObject) {
        ChatDataMessage chatDataMessage = new ChatDataMessage();
        chatDataMessage.setContent(jsonObject.getStr("content"));
        chatDataMessage.setType(jsonObject.getStr("type"));

        chatDataMessage = chatDataMessage.create();

        MessageAdapter messageAdapter = new MessageAdapter(chatDataMessage.getType());

        String content = messageAdapter.getChatDataMessage(chatDataMessage);

        jsonObject.set("content", content);

        return jsonObject;
    }
}

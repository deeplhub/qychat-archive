package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.service.adapter.MessageAdapter;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.domain.qychat.service.strategy.dto.ChatDataMessageDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 混合消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class MixedMessageStrategyImpl implements MessageStrategy {

    private final Map<String, Function<ChatDataMessageDTO, JSONObject>> actionMappings = new HashMap<>();

    {
        actionMappings.put("text", o -> this.getText(o));
        actionMappings.put("image", o -> this.getMedia(o));
        actionMappings.put("voice", o -> this.getMedia(o));
        actionMappings.put("video", o -> this.getMedia(o));
        actionMappings.put("file", o -> this.getMedia(o));
        actionMappings.put("emotion", o -> this.getMedia(o));
    }

    /**
     * 请求报文：
     * <pre>
     * {"item":[{"type":"image","content":"{\"md5sum\":\"01772d12d5ad19e31eb8f43f78167e0b\",\"filesize\":20353,\"sdkfileid\":\"CtQBMzA2ODAyMDEwMjA0NjEzMDVmMDIwMTAwMDIwNGQxNDgzYTkwMDIwMzBmNDI0MTAyMDRkNGQzNjZiNDAyMDQ2NDk1NjgyNDA0MjQzNzMwMzQzMDYzMzY2NDM5MmQ2NTY0Mzk2NTJkMzQzODM1MzMyZDYxNjM2MjM3MmQzODYxMzQzMDY0MzkzODM4NjY2NjM4MzMwMjAxMDAwMjAyNGY5MDA0MTAwMTc3MmQxMmQ1YWQxOWUzMWViOGY0M2Y3ODE2N2UwYjAyMDEwMTAyMDEwMDA0MDASOE5EZGZNVFk0T0RnMU56WTJOalF3TWprMk1GOHhNakl4T0RJd09ESTFYekUyT0RjMU1UTXhNamc9GiA3N2M5YTVlYmRmMDU5MTUwYzRmYzc2OGE4NGIzNWVmNQ==\"}"},{"type":"text","content":"{\"content\":\"@雷才德  德哥还在机房嘛\"}"}]}
     * </pre>
     *
     * @param chatDataDto
     * @return
     */
    @Override
    public String process(ChatDataMessageDTO chatDataDto) {
        List<ChatDataMessageDTO> items = chatDataDto.getItem();

        List<ChatDataMessageDTO> mixedList = items.stream().map(o -> this.getMixedMessage(o, chatDataDto.getMsgType())).collect(Collectors.toList());
        System.out.println();

        List<JSONObject> chatDataMessageList = mixedList.stream().map(this::getAction).filter(Objects::nonNull).collect(Collectors.toList());

        return JSONUtil.toJsonStr(chatDataMessageList);
    }

    private ChatDataMessageDTO getMixedMessage(ChatDataMessageDTO chatDataDto, String msgType) {
        ChatDataMessageDTO mixed = chatDataDto.createMixed();
        mixed.setMsgType(msgType);
        mixed.setType(chatDataDto.getType());
        return mixed;
    }

    private JSONObject getAction(ChatDataMessageDTO chatDataDto) {
        String type = chatDataDto.getType();

        Function<ChatDataMessageDTO, JSONObject> function = actionMappings.get(type);
        if (function == null) return null;

        return function.apply(chatDataDto);
    }


    private JSONObject getText(ChatDataMessageDTO chatDataDto) {
        JSONObject data = new JSONObject();

        data.putOpt("type", "text");
        data.putOpt("content", chatDataDto.getContent());

        return data;
    }

    private JSONObject getMedia(ChatDataMessageDTO chatDataDto) {
        String type = chatDataDto.getType();

        MessageAdapter messageAdapter = new MessageAdapter(type);

        chatDataDto.setMsgType(type);
        String process = messageAdapter.getChatDataMessage(chatDataDto);

        JSONObject data = JSONUtil.parseObj(process);
        data.putOpt("type", type);
        return data;
    }


}

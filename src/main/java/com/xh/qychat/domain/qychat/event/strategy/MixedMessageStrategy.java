package com.xh.qychat.domain.qychat.event.strategy;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.event.adapter.MessageAdapter;
import com.xh.qychat.domain.qychat.event.strategy.dto.ChatDataMessageDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 混合消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class MixedMessageStrategy implements MessageStrategy {

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
        List<JSONObject> chatDataMessageList = items.parallelStream().map(o -> this.getAction(o)).filter(Objects::nonNull).collect(Collectors.toList());

        return JSONUtil.toJsonStr(chatDataMessageList);
    }

    private JSONObject getAction(ChatDataMessageDTO chatDataDto) {
        ChatDataMessageDTO mixedDto = chatDataDto.createMixed();
        mixedDto.setType(chatDataDto.getType());

        switch (mixedDto.getType()) {
            case "text":
            case "markdown":
                return this.getMixedText(mixedDto);
            case "image":
            case "voice":
            case "video":
            case "file":
            case "emotion":
                return this.getMixedMedia(mixedDto);
            default:
                break;
        }
        return null;
    }


    private JSONObject getMixedText(ChatDataMessageDTO chatDataDto) {
        JSONObject data = new JSONObject();

        data.putOpt("type", "text");
        data.putOpt("content", chatDataDto.getContent());

        return data;
    }

    private JSONObject getMixedMedia(ChatDataMessageDTO chatDataDto) {
        String type = chatDataDto.getType();
        chatDataDto.setMsgType(type);

        MessageAdapter messageAdapter = new MessageAdapter(type);
        String process = messageAdapter.getChatDataMessage(chatDataDto);

        JSONObject data = JSONUtil.parseObj(process);
        data.putOpt("type", type);
        return data;
    }


}

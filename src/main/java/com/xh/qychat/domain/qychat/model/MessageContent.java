package com.xh.qychat.domain.qychat.model;

import cn.hutool.core.util.StrUtil;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/29
 */
@Data
public class MessageContent {
    private Long id;
    private Long seq;
    private String msgid;
    private String publickeyVer;
    private String action;
    private String fromid;
    private String tolist;
    private String roomid;
    private Date msgtime;
    private String msgtype;
    private String content;

    public static List<MessageContent> create(List<ChatDataModel> dataModels) {
        if (dataModels.isEmpty()) return new ArrayList<>(1);
        return dataModels.parallelStream().map(o -> getChatData(o)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static MessageContent getChatData(ChatDataModel chatData) {
        MessageContent messageContent = new MessageContent();

        messageContent.setSeq(chatData.getSeq());
        messageContent.setMsgid(chatData.getMsgid());
        messageContent.setAction(chatData.getAction());
        messageContent.setFromid(chatData.getFrom());
        messageContent.setPublickeyVer(chatData.getPublickeyVer());
        messageContent.setMsgtime(chatData.getMsgtime() != null ? new Date(chatData.getMsgtime()) : new Date());
        messageContent.setTolist(chatData.getTolist());
        messageContent.setRoomid(StrUtil.isNotBlank(chatData.getRoomid()) ? chatData.getRoomid() : null);
        messageContent.setMsgtype(chatData.getMsgtype());
        messageContent.setContent(chatData.getContent());

        return messageContent;
    }

}

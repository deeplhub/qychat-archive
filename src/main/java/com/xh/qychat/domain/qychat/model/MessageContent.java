package com.xh.qychat.domain.qychat.model;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "会话消息")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageContent {
    private Long id;
    private Long seq;
    private String msgid;
    private String publickeyVer;
    @ApiModelProperty("消息动作，send-发送消息,recall-撤回消息,switch-切换企业日志")
    private String action;
    @ApiModelProperty("消息发送方id")
    private String fromid;
    private String tolist;
    @ApiModelProperty("群id")
    private String roomid;
    @ApiModelProperty("消息发送时间")
    private Date msgtime;
    @ApiModelProperty("消息类型")
    private String msgtype;
    @ApiModelProperty("消息内容")
    private String content;

    public static List<MessageContent> create(List<ChatDataModel> dataModels) {
        if (dataModels.isEmpty()) return new ArrayList<>(1);
        return dataModels.parallelStream().map(o -> getChatData(o)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static MessageContent getChatData(ChatDataModel chatData) {
        // 微盘文件不做处理
        if ("qydiskfile".equals(chatData.getMsgtype())) return null;

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

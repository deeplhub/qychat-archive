package com.xh.qychat.domain.qychat.service.strategy.dto;

import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/30
 */
@Data
public class ChatDataMessageDTO extends MediaMessageDTO {

    private transient String body;
    /**
     * 媒体状态：1未加载，2加载成功，3.加载失败
     */
    private transient Integer mediaStatus = 1;

    /**
     * 会话记录消息标题
     */
    private String title;

    private String msgType;
    private String content;

    /**
     * 混合消息
     */
    private List<ChatDataMessageDTO> item;

    public ChatDataMessageDTO create() {
        ChatDataMessageDTO dto = JSONUtil.toBean(this.body, ChatDataMessageDTO.class);
        dto.setBody(this.body);
        dto.setMsgType(this.msgType);
        return dto;
    }

    public ChatDataMessageDTO createMixed() {
        ChatDataMessageDTO dto = JSONUtil.toBean(this.content, ChatDataMessageDTO.class);
        dto.setBody(this.content);
        return dto;
    }

    public String getFileNameSuffix() {
        switch (this.getMsgType()) {
            case "image":
                return this.getMd5sum() + ".png";
            case "voice":
                return this.getMd5sum() + ".mp3";
            case "video":
                return this.getMd5sum() + ".mp4";
            case "file":
                return this.getFilename();
            case "emotion":
                String fileNameSuffix = "1".equals(this.getType()) ? ".gif" : ".png";
                return this.getMd5sum() + fileNameSuffix;
            default:
                return "";
        }
    }

}

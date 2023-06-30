package com.xh.qychat.domain.qychat.service.strategy.dto;

import cn.hutool.json.JSONUtil;
import lombok.Data;

/**
 * @author H.Yang
 * @date 2023/6/30
 */
@Data
public class ChatDataMessageDTO {

    private transient String body;
    /**
     * 媒体状态：1未加载，2加载成功，3.加载失败
     */
    private transient Integer mediaStatus = 1;
    private transient String sdkfileid;
    private transient String md5sum;
    private transient MediaMessageDTO mediaMessage;
    private transient String type;
    private String content;
    private String filename;
    private Integer filesize;
    private String note;

    public ChatDataMessageDTO create() {
        MediaMessageDTO mediadDto = JSONUtil.toBean(this.body, MediaMessageDTO.class);

        this.mediaMessage = mediadDto;
        this.sdkfileid = mediadDto.getSdkfileid();
        this.md5sum = mediadDto.getMd5sum();
        this.filesize = mediadDto.getFilesize();
        this.filename = mediadDto.getFilename();

        return this;
    }

    public ChatDataMessageDTO create(MediaMessageDTO mediadDto) {

        this.sdkfileid = mediadDto.getSdkfileid();
        this.md5sum = mediadDto.getMd5sum();
        this.filesize = mediadDto.getFilesize();
        this.filename = mediadDto.getFilename();

        return this;
    }

    public ChatDataMessageDTO getContentObj() {
        return JSONUtil.toBean(this.body, ChatDataMessageDTO.class);
    }

    public String getFileNameSuffix() {
        switch (this.getType()) {
            case "image":
            case "ChatRecordImage":
                return this.md5sum + ".png";
            case "voice":
            case "ChatRecordVoice":
                return this.md5sum + ".mp3";
            case "video":
            case "ChatRecordVideo":
                return this.md5sum + ".mp4";
            case "file":
            case "ChatRecordFile":
                return this.getFilename();
            default:
                return "";
        }
    }

}

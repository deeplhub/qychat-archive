package com.xh.qychat.domain.qychat.event.strategy.dto;

import lombok.Data;

/**
 * 媒体类数据
 *
 * @author H.Yang
 * @date 2023/6/29
 */
@Data
public class MediaMessageDTO {

    private String type;
    private String sdkfileid;
    private Integer filesize;
    private Integer voiceSize;
    private Integer imagesize;
    private String filename;
    private String md5sum;
    private Long msgtime;
    private Boolean fromChatroom;

    /**
     * 音视频通话 - 通话时长，单位秒
     */
    private Integer callduration;
    /**
     * 音视频通话 - 通话类型
     * 1; //单人视频通话
     * 2; //单人语音通话
     * 3; //多人视频通话
     * 4; //多人语音通话
     */
    private Integer invitetype;

    public Integer getFilesize() {
        this.filesize = (this.voiceSize != null) ? this.voiceSize : this.filesize;
        this.filesize = (this.imagesize != null) ? this.imagesize : this.filesize;
        return this.filesize;
    }

    public String getType() {
        // 会话消息
        if (this.type != null && this.type.contains("ChatRecord")) {
            this.type = this.type.replace("ChatRecord", "").toLowerCase();
        }

        return type;
    }

    public Long getMsgtime() {
        // 默认返回单位是秒，需要把秒转毫秒
        if (this.msgtime != null) return this.msgtime * 1000;
        return this.msgtime;
    }

}

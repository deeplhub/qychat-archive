package com.xh.qychat.domain.qychat.service.strategy.dto;

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
        return this.msgtime * 1000;
    }
}

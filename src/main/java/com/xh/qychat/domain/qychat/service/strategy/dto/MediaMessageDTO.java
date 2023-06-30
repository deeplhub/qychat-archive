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
    private String filename;
    private String md5sum;

    public Integer getFilesize() {

        return (this.filesize == null && this.voiceSize != null) ? this.voiceSize : this.filesize;
    }

}

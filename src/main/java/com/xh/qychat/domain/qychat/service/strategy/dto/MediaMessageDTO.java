package com.xh.qychat.domain.qychat.service.strategy.dto;

import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/29
 */
@Data
public class MediaMessageDTO {

    private String type;
    private String contentType;
    private String content;
    private String sdkfileid;
    private Integer filesize;
    private Integer voiceSize;
    private String filename;
    private String md5sum;
    private List<MediaMessageDTO> item;

    public MediaMessageDTO getContentObj() {
        String messageType = this.type;

        MediaMessageDTO dto = JSONUtil.toBean(this.content, MediaMessageDTO.class);
        String contentType = dto.getType();

        dto.setType(messageType);
        dto.setContentType(contentType);

        return dto;
    }

    public Integer getFilesize() {

        return (this.filesize == null && this.voiceSize != null) ? this.voiceSize : this.filesize;
    }
}

package com.xh.qychat.domain.qychat.service.strategy.dto;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/29
 */
@Data
public class MixedMessageDTO {

    private String type;
    private String contentType;
    private String content;
    private String sdkfileid;
    private String filesize;
    private String md5sum;
    private List<MixedMessageDTO> item;

    public MixedMessageDTO getContentObj() {
        // TODO 注意这里的 type
        if (StrUtil.isBlank(this.content)) return null;
        String messageType = this.type;

        MixedMessageDTO dto = JSONUtil.toBean(this.content, MixedMessageDTO.class);
        String contentType = dto.getType();

        dto.setType(messageType);
        dto.setContentType(contentType);

        return dto;
    }


}

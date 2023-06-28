package com.xh.qychat.domain.qychat.model;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

/**
 * @author H.Yang
 * @date 2023/6/28
 */
@Data
public class MediaMessage {

    private transient String content;
    private transient String sdkfileid;
    private transient String md5sum;
    private Integer fileSize;
    private transient Integer voiceSize;
    private String fileName;
    private String note;
    private String type;
    private Boolean status = false;

    public MediaMessage create() {
        JSONObject jsonObject = JSONUtil.parseObj(this.content);

        this.sdkfileid = jsonObject.getStr("sdkfileid");
        this.md5sum = jsonObject.getStr("md5sum");
        this.fileSize = jsonObject.getInt("filesize");
        this.voiceSize = jsonObject.getInt("voice_size");
        this.fileName = jsonObject.getStr("filenume");
        return this;
    }
}

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
    private transient JSONObject contentObject;
    private transient String sdkfileid;
    private transient String md5sum;
    private transient Integer voiceSize;
    /**
     * 媒体状态：1未加载，2加载成功，3.加载失败
     */
    private transient Integer mediaStatus = 1;
    private String fileName;
    private Integer fileSize;
    private String note;
    /**
     * 混合消息时用到
     */
    private String type;
    /**
     * 媒体消息状态，只有为 true 时资源消息才有效
     */
    private Boolean status = false;

    public MediaMessage create() {
        JSONObject jsonObject = JSONUtil.parseObj(this.content);

        this.contentObject = jsonObject;
        this.sdkfileid = jsonObject.getStr("sdkfileid");
        this.md5sum = jsonObject.getStr("md5sum");
        this.fileSize = jsonObject.getInt("filesize");
        this.voiceSize = jsonObject.getInt("voice_size");
        this.fileName = jsonObject.getStr("filenume");
        return this;
    }
}

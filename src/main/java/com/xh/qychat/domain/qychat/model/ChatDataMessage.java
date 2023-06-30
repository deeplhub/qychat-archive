package com.xh.qychat.domain.qychat.model;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

/**
 * @author H.Yang
 * @date 2023/6/28
 */
@Data
public class ChatDataMessage {

    private transient String content;
    private transient JSONObject contentObject;
    private transient String sdkfileid;
    private transient String md5sum;
    /**
     * 媒体状态：1未加载，2加载成功，3.加载失败
     */
    private transient Integer mediaStatus = 1;
    private String fileName;
    private Integer fileSize;
    private String note;
    private String type;
    /**
     * 媒体消息状态，只有为 true 时资源消息才有效
     */
    private Boolean status;
    private Long msgtime;

    public ChatDataMessage create() {
        JSONObject jsonObject = JSONUtil.parseObj(this.content);

        this.contentObject = jsonObject;
        this.sdkfileid = jsonObject.getStr("sdkfileid");
        this.md5sum = jsonObject.getStr("md5sum");
        this.fileSize = jsonObject.getInt("filesize");
        this.fileSize = (this.fileSize == null) ? jsonObject.getInt("voice_size") : this.fileSize;
        this.fileName = jsonObject.getStr("filename");
        return this;
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
                return this.getFileName();
            default:
                return "";
        }
    }

    public Long getCreateTime() {
        // 默认返回单位是秒，需要把秒转毫秒
        return this.msgtime * 1000;
    }
}

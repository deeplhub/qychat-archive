package com.xh.qychat.domain.qychat.model;

import lombok.Data;

/**
 * @author H.Yang
 * @date 2023/6/19
 */
@Data
public class MediaModel {

    /**
     * 资源的id信息
     */
    private String sdkfileid;

    /**
     * 资源的md5值
     */
    private String md5sum;

    /**
     * 资源的文件大小
     */
    private Integer filesize;

    /**
     * 语音消息大
     */
    private Integer voiceSize;

    /**
     * 播放长度
     */
    private Integer playLength;

    /**
     * 名片所有者所在的公司名称
     */
    private String corpname;

    /**
     * 名片所有者的id，同一公司是userid，不同公司是external_userid
     */
    private String userid;

    /**
     * 表情类型，png或者gif.1表示gif 2表示png
     */
    private Integer type;

    /**
     * 表情图片宽度
     */
    private Integer width;

    /**
     * 表情图片高度
     */
    private Integer height;

    /**
     * 文件大小
     */
    private Integer imagesize;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件类型后缀
     */
    private String fileext;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 链接url地址
     */
    private String linkUrl;

    /**
     * 链接图片url
     */
    private String imageUrl;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 小程序名称
     */
    private String displayname;

    // TODO 会话记录消息item
    // TODO 待办消息
    // TODO 投票消息
    // TODO 填表消息
    // TODO 红包消息
    // TODO 在线文档消息
    // TODO MarkDown格式消息
    // TODO 图文消息
    // TODO 日程消息
    // TODO 混合消息

}

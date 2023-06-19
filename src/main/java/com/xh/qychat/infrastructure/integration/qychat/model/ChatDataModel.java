package com.xh.qychat.infrastructure.integration.qychat.model;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天消息
 *
 * @author H.Yang
 * @date 2022/1/19
 */
@Data
public class ChatDataModel extends ResponseModel {

    /**
     * 消息的seq值，标识消息的序号。再次拉取需要带上上次回包中最大的seq。
     */
    private Long seq;

    /**
     * 消息id，消息的唯一标识，企业可以使用此字段进行消息去重。msgid以_external结尾的消息，表明该消息是一条外部消息。
     */
    private String msgid;

    /**
     * 加密此条消息使用的公钥版本号
     */
    private String publickeyVer;

    /**
     * 使用publickey_ver指定版本的公钥进行非对称加密后base64加密的内容，需要业务方先base64 decode处理后，再使用指定版本的私钥进行解密，得出内容
     */
    private String encryptRandomKey;

    /**
     * 消息密文。需要业务方使用将encrypt_random_key解密得到的内容，与encrypt_chat_msg，传入sdk接口DecryptData,得到消息明文
     */
    private String encryptChatMsg;

    private List<ChatDataModel> chatdata = new ArrayList<>();

    /**
     * 消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型
     */
    private String action;

    /**
     * 消息发送方id。同一企业内容为userid，非相同企业为external_userid。消息如果是机器人发出，也为external_userid
     */
    private String from;

    /**
     * 消息接收方列表，可能是多个，同一个企业内容为userid，非相同企业为external_userid。数组
     */
    private String tolist;

    /**
     * 群聊消息的群id。如果是单聊则为空
     */
    private String roomid;

    /**
     * 消息发送时间戳，utc时间，ms单位
     */
    private Long msgtime;

    /**
     * 消息类型
     */
    private String msgtype;

    /**
     * 文本
     */
    private JSONObject text;

    /**
     * 图片
     */
    private JSONObject image;

    /**
     * 撤回消息
     */
    private JSONObject revoke;

    /**
     * 同意会话聊天内容
     */
    private JSONObject agree;

    /**
     * 语音
     */
    private JSONObject voice;

    /**
     * 视频
     */
    private JSONObject video;

    /**
     * 名片
     */
    private JSONObject card;

    /**
     * 位置
     */
    private JSONObject location;

    /**
     * 表情
     */
    private JSONObject emotion;

    /**
     * 文件
     */
    private JSONObject file;

    /**
     * 链接
     */
    private JSONObject link;

    /**
     * 小程序消息
     */
    private JSONObject weapp;

    /**
     * 会话记录消息
     */
    private JSONObject chatrecord;

    /**
     * 待办消息
     */
    private JSONObject todo;
    /**
     * 投票消息
     */
    private JSONObject vote;
    /**
     * 填表消息
     */
    private JSONObject collect;

    /**
     * 红包消息
     */
    private JSONObject redpacket;
    /**
     * 会议邀请消息
     */
    private JSONObject meeting;
    /**
     * 在线文档消息
     */
    private JSONObject doc;
    /**
     * MarkDown格式消息
     */
    private JSONObject markdown;
    /**
     * 图文消息
     */
    private JSONObject news;
    /**
     * 日程消息
     */
    private JSONObject calendar;
    /**
     * 混合消息
     */
    private JSONObject mixed;
    /**
     * 微盘文件
     */
    private JSONObject info;

}

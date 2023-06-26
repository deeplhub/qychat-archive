package com.xh.qychat.infrastructure.integration.qychat.model;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.Data;

import java.util.List;

/**
 * 客户群详情
 *
 * @author H.Yang
 * @date 2022/1/19
 */
@Data
public class ChatRoomModel extends ResponseModel {

    /**
     * 客户群ID
     */
    private String chatId;

    /**
     * 群名
     */
    private String name;

    /**
     * 群公告
     */
    private String notice;

    /**
     * 群主ID
     */
    private String owner;

    /**
     * 群的创建时间
     */
    private Long createTime;

    /**
     * 此属性字段是应用扩展字段，非返回值
     */
    private String sign;

    /**
     * 获取客户群详情
     */
    private ChatRoomModel groupChat;

    /**
     * 群成员列表
     */
    private List<RoomMemberModel> memberList;

    /**
     * 群管理员列表
     */
    private List<AdminRoomMemberModel> adminList;


    @Data
    public class RoomMemberModel {

        /**
         * 成员ID
         */
        private String userid;

        /**
         * 成员类型。
         * 1 - 企业成员
         * 2 - 外部联系人
         */
        private Integer type;

        /**
         * 入群时间
         */
        private Integer joinTime;

        /**
         * 名字。仅当 need_name = 1 时返回
         * 如果是微信用户，则返回其在微信中设置的名字
         * 如果是企业微信联系人，则返回其设置对外展示的别名或实名
         */
        private String name;

        /**
         * 此属性字段是应用扩展字段，非返回值
         * <p>
         * 用户信息md5签名，用于判断数据是否一致，保存或更新需要更新签名信息。sign=成员ID+入群时间+入群方式+邀请者+昵称+名字
         */
        private String sign;

        public String getSign() {
            String verify = this.getVerify();
            return (StrUtil.isNotBlank(verify)) ? SecureUtil.md5(verify) : "";
        }

        public String getVerify() {
            String sb = this.isEmpty(this.userid) +
                    this.isEmpty(this.type) +
                    this.isEmpty(this.name);

            return sb.replace(" ", "");
        }

        private String isEmpty(Object obj) {
            return (obj != null) ? obj + "" : "";
        }
    }

    @Data
    public class AdminRoomMemberModel {
        private String userid;
    }

    public String getSign() {
        String verify = this.getVerify();
        return (StrUtil.isNotBlank(verify)) ? SecureUtil.md5(verify) : "";
    }

    public String getVerify() {
        String sb = this.isEmpty(this.chatId) +
                this.isEmpty(this.name) +
                this.isEmpty(this.notice) +
                this.isEmpty(this.owner);

        return sb.replace(" ", "");
    }

    private String isEmpty(Object obj) {
        return (obj != null) ? obj + "" : "";
    }

}

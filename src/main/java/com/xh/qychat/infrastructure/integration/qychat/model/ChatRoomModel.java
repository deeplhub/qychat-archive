package com.xh.qychat.infrastructure.integration.qychat.model;

import com.xh.qychat.infrastructure.util.SignUtils;
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
         * 名字。仅当 need_name = 1 时返回
         * 如果是微信用户，则返回其在微信中设置的名字
         * 如果是企业微信联系人，则返回其设置对外展示的别名或实名
         */
        private String name;

        /**
         * 此属性字段是应用扩展字段，非返回值
         * <p>
         * 用户信息md5签名，用于判断数据是否一致
         */
        private String sign;

        public String getSign() {
            return SignUtils.getSign(this.userid, this.type, this.name);
        }
    }

    @Data
    public class AdminRoomMemberModel {
        private String userid;
    }

    public Long getCreateTime() {
        // 默认返回单位是秒，需要把秒转毫秒
        if (createTime != null) return createTime * 1000;
        return createTime;
    }

    public String getSign() {
        return SignUtils.getSign(this.chatId, this.name, this.notice, this.owner);
    }

}

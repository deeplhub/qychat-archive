package com.xh.qychat.infrastructure.integration.qychat.model;

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
         * 群成员id
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
        private String joinTime;

        /**
         * 入群方式。
         * 1 - 由群成员邀请入群（直接邀请入群）
         * 2 - 由群成员邀请入群（通过邀请链接入群）
         * 3 - 通过扫描群二维码入群
         */
        private Integer joinScene;

        /**
         * 邀请者。目前仅当是由本企业内部成员邀请入群时会返回该值
         */
        private String invitor;

        /**
         * 在群里的昵称
         */
        private String groupNickname;

        /**
         * 名字。仅当 need_name = 1 时返回
         * 如果是微信用户，则返回其在微信中设置的名字
         * 如果是企业微信联系人，则返回其设置对外展示的别名或实名
         */
        private String name;
    }

    @Data
    public class AdminRoomMemberModel {
        private String userid;
    }

}

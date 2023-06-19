package com.xh.qychat.infrastructure.integration.qychat.model;

import lombok.Data;

/**
 * 客户详情（外部联系人）
 *
 * @author H.Yang
 * @date 2023/6/19
 */
@Data
public class CustomerModel extends ResponseModel {

    /**
     * 外部联系人的名称
     */
    private String name;

    /**
     * 外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户
     */
    private Integer type;

    /**
     * 外部联系人头像
     */
    private String avatar;

    /**
     * 外部联系人性别 0-未知 1-男性 2-女性
     */
    private Integer gender;

    /**
     * 外部联系人在微信开放平台的唯一身份标识（微信unionid），通过此字段企业可将外部联系人与公众号/小程序用户关联起来。
     * 仅当联系人类型是微信用户，且企业绑定了微信开发者ID有此字段
     */
    private String unionid;

    /**
     * 外部联系人的userid
     */
    private String externalUserid;

    /**
     * 外部联系人详情
     */
    private CustomerModel externalContact;


}

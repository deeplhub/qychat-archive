package com.xh.qychat.infrastructure.integration.qychat.model;

import lombok.Data;

/**
 * 人员详情
 * <p>
 * 成员详情（内部联系人）+客户详情（外部联系人）
 *
 * @author H.Yang
 * @date 2023/6/27
 */
@Data
public class PersonnelModel extends ResponseModel {

    /**
     * 成员UserID
     */
    private String userid;

    /**
     * 成员名称
     */
    private String name;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 职务信息
     */
    private String position;

    /**
     * 性别。0表示未定义，1表示男性，2表示女性。
     */
    private Integer gender;


    /**
     * 邮箱
     */
    private String email;

    /**
     * 企业邮箱
     */
    private String bizMail;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 座机
     */
    private String telephone;

    /**
     * 别名
     */
    private String alias;

    /**
     * 备注
     */
    private String note;

    /**
     * 外部联系人详情
     */
    private PersonnelModel externalContact;
}

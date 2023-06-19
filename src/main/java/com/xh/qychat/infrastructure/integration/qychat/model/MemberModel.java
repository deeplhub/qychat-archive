package com.xh.qychat.infrastructure.integration.qychat.model;

import lombok.Data;

/**
 * 成员详情（内部联系人）
 *
 * @author H.Yang
 * @date 2023/6/19
 */
@Data
public class MemberModel extends ResponseModel {

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
     * 性别。0表示未定义，1表示男性，2表示女性。
     */
    private Integer gender;

    /**
     * 职务信息
     */
    private String position;

    /**
     * 成员所属部门id列表，仅返回该应用有查看权限的部门id；
     */
    private String department;

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
     * 激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。
     */
    private Integer status;

    /**
     * 对外职务，如果设置了该值，则以此作为对外展示的职务，否则以position来展示。
     */
    private String externalPosition;

    /***********************************************************************************************************************************************************
     *
     *
     * 外部联系人
     *
     *
     **********************************************************************************************************************************************************/

    private CustomerModel customerModel;
}

package com.xh.qychat.domain.qychat.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.xh.qychat.infrastructure.util.SignUtils;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 企业微信成员信息表
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Data
@TableName("qychat_member")
public class MemberEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 成员ID
     */
    private String userId;

    /**
     * 名称
     */
    private String name;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 成员类型：1企业成员，2外部联系人
     */
    private Integer type;

    /**
     * 0-未知 1-男性 2-女性
     */
    private Integer gender;

    /**
     * 外部联系人所在企业的职位-联系人类型是企业微信用户时有此字段
     */
    private String position;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 企业邮箱
     */
    private String bizMail;

    /**
     * 座机
     */
    private String telephone;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    /**
     * 用户信息md5签名，用于判断数据是否一致，保存或更新需要更新签名信息。sign=成员ID+入群时间+入群方式+邀请者+昵称+名字
     */
    @TableField(exist = false)
    private String sign;

    public String getSign() {
        return SignUtils.getSign(this.userId, this.type, this.name);
    }
}

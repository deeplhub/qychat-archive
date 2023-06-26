package com.xh.qychat.domain.qychat.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
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
public class MemberEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 头像
     */
    private String avatar;

    /**
     * 0-企业成员，1-该外部联系人是微信用户，2-该外部联系人是企业微信用户
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
     * 外部联系人所在企业的简称-同上
     */
    private String corpName;

    /**
     * 外部联系人所在企业的主体名称-同上
     */
    private String corpFullName;

    /**
     * 添加了此外部联系人的企业成员userid
     */
    private String followUserid;

    /**
     * 该成员对此外部联系人的备注
     */
    private String followRemark;

    /**
     * 该成员对此外部联系人的描述
     */
    private String followDescription;

    /**
     * 该成员对此客户备注的企业名称
     */
    private String followRemarkCorpName;

    /**
     * 该成员对此客户备注的手机号码
     */
    private String followRemarkMobiles;

    /**
     * 用户信息md5签名，用于判断数据是否一致，保存或更新需要更新签名信息。sign=成员ID+入群时间+入群方式+邀请者+昵称+名字
     */
    private String sign;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}

package com.xh.qychat.domain.qychat.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 * 微信群与用户关系
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Data
@TableName("qychat_room_member")
public class RoomMemberEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 成员ID
     */
    private String userId;

    /**
     * 企业微信客户群ID
     */
    private String chatId;


}

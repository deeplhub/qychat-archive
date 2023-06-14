package com.xh.qychat.domain.qychat.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;

/**
 * <p>
 * 微信群详情
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Data
@TableName("qychat_chat_room")
public class ChatRoomEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 群ID
     */
    private String chatId;

    /**
     * 群名称
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
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 群创建时间
     */
    private Date groupCreateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public String getVerify() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.isEmpty(chatId)).append("|");
        sb.append(this.isEmpty(name)).append("|");
        sb.append(this.isEmpty(notice)).append("|");
        sb.append(this.isEmpty(owner)).append("|");

        return sb.toString().replace(" ", "");
    }

    private String isEmpty(Object obj) {
        if (obj != null) {
            return obj + "";
        }
        return "";
    }
}

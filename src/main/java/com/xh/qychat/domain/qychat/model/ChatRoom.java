package com.xh.qychat.domain.qychat.model;

import cn.hutool.core.date.DateUtil;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
@Data
public class ChatRoom {
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
    private Date createTime;

    /**
     * 此属性字段是应用扩展字段，非返回值
     */
    private String sign;

    public static List<ChatRoom> create(Set<ChatRoomModel> chatRooms) {

        return chatRooms.parallelStream().map(o -> getChatRoom(o)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static ChatRoom getChatRoom(ChatRoomModel chatRoomModel) {
        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setChatId(chatRoomModel.getChatId());
        chatRoom.setName(chatRoomModel.getName());
        chatRoom.setNotice(chatRoomModel.getNotice());
        chatRoom.setOwner(chatRoomModel.getOwner());
        chatRoom.setCreateTime(DateUtil.date(chatRoomModel.getCreateTime()));
        chatRoom.setSign(chatRoomModel.getSign());

        return chatRoom;
    }
}
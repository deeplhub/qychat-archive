package com.xh.qychat.domain.qychat.model;

import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/26
 */
@Data
public class ChatRoomTreeNodeModel {

    /**
     * 客户群ID
     */
    private String chatId;

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
     * 用户信息md5签名，用于判断数据是否一致，保存或更新需要更新签名信息。sign=成员ID+入群时间+入群方式+邀请者+昵称+名字
     */
    private String sign;

    private List<ChatRoomTreeNodeModel> children;


    public List<ChatRoomTreeNodeModel> createTreeNode(Set<ChatRoomModel> list) {
        List<ChatRoomTreeNodeModel> treeNode = new LinkedList<>();
        for (ChatRoomModel chatRoom : list) {
            ChatRoomTreeNodeModel node = new ChatRoomTreeNodeModel();
            node.setChatId(chatRoom.getChatId());
            node.setChildren(chatRoom.getMemberList().parallelStream().map(member -> {
                        ChatRoomTreeNodeModel children = new ChatRoomTreeNodeModel();

                        children.setUserid(member.getUserid());
                        children.setType(member.getType());
                        children.setName(member.getName());
                        children.setSign(member.getSign());

                        return children;
                    }).collect(Collectors.toList())
            );

            treeNode.add(node);
        }

        return treeNode;
    }
}

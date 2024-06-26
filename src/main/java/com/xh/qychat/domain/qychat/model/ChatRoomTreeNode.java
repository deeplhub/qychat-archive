package com.xh.qychat.domain.qychat.model;

import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.util.SignUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/26
 */
@Data
public class ChatRoomTreeNode {

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
     * 用户信息md5签名，用于判断数据是否一致
     */
    private String sign = "";

    private List<ChatRoomTreeNode> children;


    public static List<ChatRoomTreeNode> createTreeNode(Set<ChatRoomModel> list) {
        if (list.isEmpty()) return new ArrayList<>(1);

        List<ChatRoomTreeNode> treeNode = new LinkedList<>();
        ChatRoomTreeNode node = null;
        for (ChatRoomModel chatRoom : list) {
            node = new ChatRoomTreeNode();

            List<ChatRoomTreeNode> memberList = chatRoom.getMemberList().parallelStream().map(member -> getChatRoomTreeNode(chatRoom.getChatId(), member)).collect(Collectors.toList());
            String memberSign = memberList.parallelStream().map(o -> o.getSign()).collect(Collectors.joining(""));

            node.setChatId(chatRoom.getChatId());
            node.setChildren(memberList);
            node.setSign(SignUtils.getSign(chatRoom.getChatId(), memberSign));

            treeNode.add(node);
        }

        return treeNode;
    }

    public static ChatRoomTreeNode createTreeNode(ChatRoomModel chatRoomModel) {
        ChatRoomTreeNode node = new ChatRoomTreeNode();

        node.setChatId(chatRoomModel.getChatId());
        node.setChildren(chatRoomModel.getMemberList().parallelStream().map(member -> getChatRoomTreeNode(chatRoomModel.getChatId(), member)).collect(Collectors.toList()));

        return node;
    }

    private static ChatRoomTreeNode getChatRoomTreeNode(String chatId, ChatRoomModel.RoomMemberModel member) {
        ChatRoomTreeNode node = new ChatRoomTreeNode();

        node.setChatId(chatId);
        node.setUserid(member.getUserid());
        node.setType(member.getType());
        node.setName(member.getName());
        node.setSign(member.getSign());

        return node;
    }
}

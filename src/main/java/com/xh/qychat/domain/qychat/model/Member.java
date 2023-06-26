package com.xh.qychat.domain.qychat.model;

import cn.hutool.core.lang.Assert;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
@Data
@NoArgsConstructor
public class Member {

    private Long memberId;
    private String chatId;
    private String userid;
    private String sign;
    private Integer type;
    private String name;
    private Date createTime;

    private Set<ChatRoomModel> chatRoomModelList;

    public Member(Set<ChatRoomModel> chatRoomModelList) {
        this.chatRoomModelList = chatRoomModelList;
    }

    public Set<String> listChatId() {
        Assert.isTrue(this.chatRoomModelList != null, "客户群详情不能为空");
        return this.chatRoomModelList.parallelStream().map(o -> o.getChatId()).collect(Collectors.toSet());
    }

    public static Set<String> listUserId(Member member) {
        return member.getChatRoomModelList().parallelStream()
                .map(ChatRoomModel::getMemberList)// 将memberList属性映射成为一个流
                .flatMap(Collection::stream) // 扁平化
                .map(ChatRoomModel.RoomMemberModel::getUserid)
                .collect(Collectors.toSet());
    }

    public Set<Member> listMember() {
        Assert.isTrue(this.chatRoomModelList != null, "客户群详情不能为空");
        Set<Member> memberSet = new HashSet<>();

        for (ChatRoomModel chatRoomModel : this.chatRoomModelList) {
            chatRoomModel.getMemberList().parallelStream().filter(o -> o != null).forEach(item -> {
                Member member = new Member();

                member.setChatId(chatRoomModel.getChatId());
                member.setUserid(item.getUserid());
                member.setType(item.getType());
                member.setName(item.getName());
                member.setSign(item.getSign());

                memberSet.add(member);
            });
        }

        return memberSet;
    }
}

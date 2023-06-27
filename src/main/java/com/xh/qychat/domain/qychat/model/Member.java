package com.xh.qychat.domain.qychat.model;

import com.xh.qychat.infrastructure.integration.qychat.model.PersonnelModel;
import lombok.Data;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
@Data
public class Member {

    private String userId;
    private String avatar;
    private Integer gender;
    private String position;
    private String telephone;

    public static Member create(PersonnelModel personnel) {
        Member member = new Member();

        member.setUserId(personnel.getUserid());
        member.setAvatar(personnel.getAvatar());
        member.setGender(personnel.getGender());
        member.setPosition(personnel.getPosition());
        member.setTelephone(personnel.getTelephone());

        return member;
    }
}

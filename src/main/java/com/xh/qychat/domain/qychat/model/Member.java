package com.xh.qychat.domain.qychat.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xh.qychat.infrastructure.integration.qychat.model.PersonnelModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
@Data
@ApiModel(value = "群客户")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Member {

    private String id;
    @ApiModelProperty(value = "成员ID")
    private String userId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "成员类型：1企业成员，2外部联系人")
    private Integer type;
    @ApiModelProperty(value = "性别：0-未知 1-男性 2-女性")
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

package com.xh.qychat.domain.qychat.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 企业微信成员信息表 服务类
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
public interface MemberService extends IService<MemberEntity> {

    List<MemberEntity> listByCharId(String chatId);

    List<MemberEntity> listByUserId(Set<String> userIds);
}

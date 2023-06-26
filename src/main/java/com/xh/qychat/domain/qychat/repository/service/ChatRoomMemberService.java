package com.xh.qychat.domain.qychat.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomMemberEntity;

import java.util.Set;

/**
 * <p>
 * 微信群与用户关系 服务类
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
public interface ChatRoomMemberService extends IService<ChatRoomMemberEntity> {

    boolean dissolution(String chatId, Set<String> userIds);
}

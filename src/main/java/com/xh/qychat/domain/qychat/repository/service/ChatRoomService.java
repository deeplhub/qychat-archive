package com.xh.qychat.domain.qychat.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomEntity;

/**
 * <p>
 * 微信群详情 服务类
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
public interface ChatRoomService extends IService<ChatRoomEntity> {

    ChatRoomEntity getByChatId(String chatId);
}

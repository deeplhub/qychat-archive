package com.xh.qychat.domain.qychat.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomEntity;
import com.xh.qychat.domain.qychat.repository.mapper.ChatRoomMapper;
import com.xh.qychat.domain.qychat.repository.service.ChatRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 微信群详情 服务实现类
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Service
public class ChatRoomServiceImpl extends ServiceImpl<ChatRoomMapper, ChatRoomEntity> implements ChatRoomService {

    @Override
    public List<ChatRoomEntity> listByChatId(Set<String> chatIds) {
        QueryWrapper<ChatRoomEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().select(ChatRoomEntity::getId, ChatRoomEntity::getChatId, ChatRoomEntity::getSign);
        queryWrapper.lambda().in(ChatRoomEntity::getChatId, chatIds);

        return super.list(queryWrapper);
    }
}

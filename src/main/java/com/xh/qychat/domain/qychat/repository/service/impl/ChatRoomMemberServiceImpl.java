package com.xh.qychat.domain.qychat.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomMemberEntity;
import com.xh.qychat.domain.qychat.repository.mapper.ChatRoomMemberMapper;
import com.xh.qychat.domain.qychat.repository.service.ChatRoomMemberService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 微信群与用户关系 服务实现类
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Service
public class ChatRoomMemberServiceImpl extends ServiceImpl<ChatRoomMemberMapper, ChatRoomMemberEntity> implements ChatRoomMemberService {

    @Override
    public boolean removeByChatId(String chatId, Set<String> userIds) {
        QueryWrapper<ChatRoomMemberEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(ChatRoomMemberEntity::getChatId, chatId);
        queryWrapper.lambda().notIn(ChatRoomMemberEntity::getUserId, userIds);

        return super.remove(queryWrapper);
    }

    @Override
    public boolean removeByChatId(String chatId) {
        QueryWrapper<ChatRoomMemberEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(ChatRoomMemberEntity::getChatId, chatId);

        return super.remove(queryWrapper);
    }

    @Override
    public boolean removeBatchByChatId(Set<String> chatIds) {
        QueryWrapper<ChatRoomMemberEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().in(ChatRoomMemberEntity::getChatId, chatIds);

        return super.remove(queryWrapper);
    }

}

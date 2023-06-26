package com.xh.qychat.domain.qychat.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xh.qychat.domain.qychat.repository.mapper.ChatRoomMemberMapper;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomMemberEntity;
import com.xh.qychat.domain.qychat.repository.service.ChatRoomMemberService;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<ChatRoomMemberEntity> listByChatId(Set<String> chatIds) {
        // 处理in条件超过1000个字符的办法处理in条件超过1000个字符的办法
        return super.baseMapper.listByChatId(chatIds);
    }
}

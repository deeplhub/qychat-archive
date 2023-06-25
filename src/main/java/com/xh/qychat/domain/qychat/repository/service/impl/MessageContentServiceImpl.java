package com.xh.qychat.domain.qychat.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.mapper.MessageContentMapper;
import com.xh.qychat.domain.qychat.repository.service.MessageContentService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MessageContentServiceImpl extends ServiceImpl<MessageContentMapper, MessageContentEntity> implements MessageContentService {

    @Override
    public Long getMaxSeq() {

        return super.baseMapper.getMaxSeq();
    }

    @Override
    public Set<String> listRoomIdGoupByRoomId() {
        QueryWrapper<MessageContentEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().isNotNull(MessageContentEntity::getRoomid);
        queryWrapper.lambda().groupBy(MessageContentEntity::getRoomid);

        return super.baseMapper.listRoomIdGoupByRoomId(queryWrapper);
    }
}





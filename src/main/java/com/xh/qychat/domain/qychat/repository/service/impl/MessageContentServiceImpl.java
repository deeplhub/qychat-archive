package com.xh.qychat.domain.qychat.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.mapper.MessageContentMapper;
import com.xh.qychat.domain.qychat.repository.service.MessageContentService;
import org.springframework.stereotype.Service;

@Service
public class MessageContentServiceImpl extends ServiceImpl<MessageContentMapper, MessageContentEntity> implements MessageContentService {

    @Override
    public Long getMaxSeq() {

        return super.baseMapper.getMaxSeq();
    }

    @Override
    public Page<String> pageListRoomIdGoupByRoomId(Integer pageNum, Integer limit) {
        QueryWrapper<MessageContentEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().isNotNull(MessageContentEntity::getRoomid);
        queryWrapper.lambda().groupBy(MessageContentEntity::getRoomid);

        return super.baseMapper.pageListRoomIdGoupByRoomId(new Page<>(pageNum, limit), queryWrapper);
    }

    @Override
    public Page<MessageContentEntity> pageListByChatId(String chatId, Integer pageNum, Integer limit) {
        QueryWrapper<MessageContentEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(MessageContentEntity::getRoomid, chatId);
        queryWrapper.lambda().orderByDesc(MessageContentEntity::getSeq);

        return super.page(new Page<>(pageNum, limit), queryWrapper);
    }


}





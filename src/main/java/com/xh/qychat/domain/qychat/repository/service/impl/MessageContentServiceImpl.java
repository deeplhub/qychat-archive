package com.xh.qychat.domain.qychat.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.mapper.MessageContentMapper;
import com.xh.qychat.domain.qychat.repository.service.MessageContentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public boolean updateById(String content,Integer mediaStatus, Long id) {
        UpdateWrapper<MessageContentEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(MessageContentEntity::getId, id);
        updateWrapper.lambda().set(MessageContentEntity::getContent, content);
        updateWrapper.lambda().set(MessageContentEntity::getMediaStatus, mediaStatus);

        return super.update(updateWrapper);
    }


}





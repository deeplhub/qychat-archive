package com.xh.qychat.domain.qychat.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xh.qychat.domain.qychat.repository.mapper.MessageContentMapper;
import com.xh.qychat.domain.qychat.repository.service.MessageContentService;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import org.springframework.stereotype.Service;

@Service
public class MessageContentServiceImpl extends ServiceImpl<MessageContentMapper, MessageContentEntity> implements MessageContentService {

    @Override
    public Long getMaxSeq() {

        return super.baseMapper.getMaxSeq();
    }
}





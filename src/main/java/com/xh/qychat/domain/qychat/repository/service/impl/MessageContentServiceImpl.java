package com.xh.qychat.domain.qychat.repository.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.repository.mapper.MessageContentMapper;
import com.xh.qychat.domain.qychat.repository.service.MessageContentService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<MessageContentEntity> listByChatId(String chatId, String msgtime) {
        QueryWrapper<MessageContentEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().select(MessageContentEntity::getId,
                MessageContentEntity::getAction,
                MessageContentEntity::getFromid,
                MessageContentEntity::getRoomid,
                MessageContentEntity::getMsgtime,
                MessageContentEntity::getMsgtype,
                MessageContentEntity::getContent,
                MessageContentEntity::getMediaStatus

        );
        queryWrapper.lambda().eq(MessageContentEntity::getRoomid, chatId)
                .between(MessageContentEntity::getMsgtime, DateUtil.beginOfDay(DateUtil.parse(msgtime, "yyyy-MM-dd")), DateUtil.endOfDay(DateUtil.parse(msgtime, "yyyy-MM-dd")))
                .orderByDesc(MessageContentEntity::getMsgtime);

        return super.list(queryWrapper);
    }

    @Override
    public MessageContentEntity getByChatId(String chatId, String msgtime) {
        QueryWrapper<MessageContentEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.select("max(msgtime) AS msgtime")
                .lambda()
                .eq(MessageContentEntity::getRoomid, chatId)
                .le(MessageContentEntity::getMsgtime, msgtime);

        return super.getOne(queryWrapper);
    }


}





package com.xh.qychat.domain.qychat.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface MessageContentMapper extends BaseMapper<MessageContentEntity> {


    /**
     * 获取消息记录开始的seq值
     *
     * @return
     */
    @Select("SELECT MAX(seq) FROM qychat_message_content ")
    Long getMaxSeq();

    @Select("SELECT roomid FROM qychat_message_content ${ew.customSqlSegment}")
    Set<String> listRoomIdGoupByRoomId(@Param(Constants.WRAPPER) QueryWrapper<MessageContentEntity> queryWrapper);
}





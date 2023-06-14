package com.xh.qychat.domain.qychat.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageContentMapper extends BaseMapper<MessageContentEntity> {


    /**
     * 获取消息记录开始的seq值
     *
     * @return
     */
    @Select("SELECT MAX(seq) FROM qychat_message_content ")
    Long getMaxSeq();
}





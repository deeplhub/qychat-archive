package com.xh.qychat.domain.qychat.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 分页查询消息内容中所有群ID
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("SELECT roomid FROM qychat_message_content ${ew.customSqlSegment}")
    Page<String> pageListRoomIdGoupByRoomId(@Param("page") Page<MessageContentEntity> page, @Param(Constants.WRAPPER) QueryWrapper<MessageContentEntity> queryWrapper);
}





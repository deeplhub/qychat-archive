package com.xh.qychat.domain.qychat.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomMemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 微信群与用户关系 Mapper 接口
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Mapper
public interface ChatRoomMemberMapper extends BaseMapper<ChatRoomMemberEntity> {

    List<ChatRoomMemberEntity> listByChatId(@Param("chatIds") Set<String> chatIds);
}

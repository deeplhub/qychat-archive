package com.xh.qychat.domain.qychat.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.qychat.domain.qychat.repository.entity.RoomMemberEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 微信群与用户关系 Mapper 接口
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Mapper
public interface RoomMemberMapper extends BaseMapper<RoomMemberEntity> {

}

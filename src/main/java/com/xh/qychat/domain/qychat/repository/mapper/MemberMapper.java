package com.xh.qychat.domain.qychat.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 企业微信成员信息表
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Mapper
public interface MemberMapper extends BaseMapper<MemberEntity> {

    List<Member> listByCharId(@Param("charIds") Set<String> charIds);

    List<MemberEntity> listEntityByCharId(String charId);
}

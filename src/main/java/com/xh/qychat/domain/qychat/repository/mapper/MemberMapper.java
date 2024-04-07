package com.xh.qychat.domain.qychat.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 根据群ID查询成员列表
     *
     * @param charId
     * @return
     */
    List<MemberEntity> listByCharId(String charId);

}

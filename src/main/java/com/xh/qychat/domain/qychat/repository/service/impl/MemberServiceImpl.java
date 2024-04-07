package com.xh.qychat.domain.qychat.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import com.xh.qychat.domain.qychat.repository.mapper.MemberMapper;
import com.xh.qychat.domain.qychat.repository.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 企业微信成员信息表 服务实现类
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, MemberEntity> implements MemberService {

    @Override
    public List<MemberEntity> listByCharId(String chatId) {

        return super.baseMapper.listByCharId(chatId);
    }

    @Override
    public List<MemberEntity> listByUserId(Set<String> userIds) {
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(MemberEntity::getUserId, userIds);

        return super.list(queryWrapper);
    }

    @Override
    public MemberEntity getByUserId(String userId) {
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MemberEntity::getUserId, userId);

        return super.getOne(queryWrapper);
    }

}

package com.xh.qychat.domain.qychat.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xh.qychat.domain.qychat.model.Member;
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
    public List<Member> listByCharId(Set<String> userIds) {
        // 处理in条件超过1000个字符的办法处理in条件超过1000个字符的办法
        return super.baseMapper.listByCharId(userIds);
    }
}

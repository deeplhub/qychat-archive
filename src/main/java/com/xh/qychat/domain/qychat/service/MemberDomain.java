package com.xh.qychat.domain.qychat.service;

import com.xh.qychat.domain.qychat.model.Member;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
public interface MemberDomain {

    boolean saveOrUpdateBatch(Member member);
}

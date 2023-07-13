package com.xh.qychat.application.service.impl;

import com.xh.qychat.application.event.ResponseEvent;
import com.xh.qychat.application.service.MemberApplication;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.domain.qychat.service.MemberDomain;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.common.model.Result;
import com.xh.qychat.infrastructure.integration.qychat.model.PersonnelModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/7/13
 */
@Service
public class MemberApplicationImpl implements MemberApplication {
    @Resource
    private MemberDomain memberDomain;
    @Resource
    private TaskDomainService taskDomainService;

    @Override
    public Result pullPersonnel(String userId) {
        PersonnelModel personnel = taskDomainService.getPersonnel(userId);

        boolean isSuccess = memberDomain.saveOrUpdate(Member.create(personnel));
        return ResponseEvent.reply(isSuccess);
    }

    @Override
    public Result listByChatId(String chatId) {

        return Result.succeed(memberDomain.listByCharId(chatId));
    }
}

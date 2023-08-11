package com.xh.qychat.controller.facade;

import com.xh.qychat.application.service.MemberApplication;
import com.xh.qychat.domain.qychat.model.Member;
import com.xh.qychat.infrastructure.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/7/13
 */
@Api(tags = "群用户管理")
@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberApplication memberApplication;

    /**
     * 拉取指定人员详情
     *
     * @param userId
     * @return
     */
    @GetMapping("/pullPersonnel")
    Result pullPersonnel(String userId) {

        return memberApplication.pullPersonnel(userId);
    }

    @ApiOperation(value = "根据群ID查询成员列表")
    @GetMapping("/listByChatId/{chatId}")
    Result<Member> listByChatId(@PathVariable("chatId") String chatId) {

        return memberApplication.listByChatId(chatId);
    }
}

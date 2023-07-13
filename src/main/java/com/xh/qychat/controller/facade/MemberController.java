package com.xh.qychat.controller.facade;

import com.xh.qychat.application.service.MemberApplication;
import com.xh.qychat.infrastructure.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/7/13
 */
@Slf4j
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

    /**
     * 根据群ID查询成员列表
     *
     * @param chatId
     * @return
     */
    @GetMapping("/listByChatId")
    Result listByChatId(String chatId) {

        return memberApplication.listByChatId(chatId);
    }
}

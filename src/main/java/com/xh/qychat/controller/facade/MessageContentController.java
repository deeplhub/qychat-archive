package com.xh.qychat.controller.facade;

import com.xh.qychat.application.service.MessageContentApplication;
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
@RequestMapping("/message")
public class MessageContentController {

    @Resource
    private MessageContentApplication messageContentApplication;

    /**
     * 根据群ID分页查询会话消息
     *
     * @param chatId
     * @param pageNum
     * @param limit
     * @return
     */
    @GetMapping("/listChatRoom")
    Result listChatRoom(String chatId, Integer pageNum, Integer limit) {

        return messageContentApplication.listByChatId(chatId, pageNum, limit);
    }
}

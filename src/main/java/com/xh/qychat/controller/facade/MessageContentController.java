package com.xh.qychat.controller.facade;

import com.xh.qychat.application.service.MessageContentApplication;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.infrastructure.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/7/13
 */
@Api(tags = "消息管理")
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
    @ApiOperation(value = "根据群ID分页查询会话消息")
    @GetMapping("/listChatRoom")
    Result<MessageContent> listChatRoom(String chatId, Integer pageNum, Integer limit) {

        return messageContentApplication.listByChatId(chatId, pageNum, limit);
    }
}
